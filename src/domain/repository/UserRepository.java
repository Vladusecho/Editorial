package domain.repository;

import domain.model.User;

import java.util.List;

public interface UserRepository {
    void addUser(User user);

    void editUser(User user);

    void deleteUser(int userId);

    User getUserById(int userId);

    boolean existsById(int userId);

    List<User> getUsers();
}
