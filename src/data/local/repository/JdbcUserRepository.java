package data.local.repository;

import domain.model.User;
import domain.repository.UserRepository;
import data.local.database.DatabaseConnectionFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements UserRepository {
    private final DatabaseConnectionFactory connectionFactory;

    public JdbcUserRepository(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void addUser(User user) {
        String sql = """
                INSERT INTO users (username, email, password_hash, role_id)
                VALUES (?, ?, ?, (SELECT id FROM roles WHERE code = ?))
                RETURNING id
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole().name());

            try (var res = statement.executeQuery()) {
                if (!res.next()) throw new IllegalStateException("Database didn't return anything");

                user.setId(res.getInt("id"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't add user", e);
        }
    }

    @Override
    public void editUser(User user) {
        String sql = """
                UPDATE users 
                SET
                    username = ?,
                    email = ?,
                    password_hash = ?,
                    role_id = (SELECT id FROM roles WHERE code = ?)
                WHERE id = ?
                """;

        try (
                var connection = connectionFactory.openConnection();
                var statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setString(4, user.getRole().name());
            statement.setInt(5, user.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) throw new IllegalArgumentException("Couldn't find the user");
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't edit the user");
        }
    }

    @Override
    public void deleteUser(int userId) {
        String sql = """
                DELETE FROM users WHERE id = ?
                """;

        try (var connection = connectionFactory.openConnection();
             var statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, userId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) throw new IllegalArgumentException("Couldn't find such user");

        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't delete user", e);
        }
    }

    @Override
    public User getUserById(int userId) {
        String sql = """
                SELECT
                    users.id,
                    users.username,
                    users.email,
                    users.password_hash,
                    roles.code AS role_code
                FROM users
                JOIN roles ON roles.id = users.role_id
                WHERE users.id = ?
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setInt(1, userId);

            try (var resSet = statement.executeQuery()) {
                if (!resSet.next()) throw new IllegalStateException("Database didn't return user");

                User returnUser = new User(resSet.getInt("id"),
                        resSet.getString("username"),
                        resSet.getString("email"),
                        resSet.getString("password_hash"),
                        User.Role.valueOf(resSet.getString("role_code")));
                return returnUser;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't get user", e);
        }
    }

    @Override
    public boolean existsById(int userId) {
        String sql = """
                SELECT 1
                FROM users
                WHERE id = ?
                """;

        try (
                var connection = connectionFactory.openConnection();
                var statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, userId);

            try (var result = statement.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't check if user exists", e);
        }
    }

    @Override
    public List<User> getUsers() {
        String sql = """
                SELECT * FROM users
                """;

        try (var connection = connectionFactory.openConnection();
             var statement = connection.prepareStatement(sql)
        ) {

            try (var resSet = statement.executeQuery()) {
                List<User> returnUsersList = new ArrayList<User>();

                while (resSet.next()) {
                    returnUsersList.add(getUserById(resSet.getInt("id")));
                }

                return returnUsersList;
            }
        }
        catch (SQLException e) {
            throw new IllegalStateException("Couldn't get usesr");
        }

    }
}
