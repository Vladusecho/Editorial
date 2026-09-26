package data.local;

import data.local.database.DatabaseConfig;
import data.local.database.DatabaseConnectionFactory;
import data.local.database.DatabaseMigrator;
import data.local.repository.JdbcArticleRepository;
import data.local.repository.JdbcUserRepository;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.sql.SQLException;

public abstract class   JdbcTestBase {
    private static EmbeddedPostgres postgres;

    protected static DatabaseConnectionFactory connectionFactory;
    protected static JdbcUserRepository userRepository;
    protected static JdbcArticleRepository articleRepository;

    @BeforeAll
    static void startDatabase() throws IOException {
        if (postgres == null) {
            postgres = EmbeddedPostgres.builder().start();
        }

        DatabaseConfig config = new DatabaseConfig(
                postgres.getJdbcUrl("postgres", "postgres"),
                "postgres",
                "postgres"
        );
        new DatabaseMigrator(config).migrate();

        connectionFactory = new DatabaseConnectionFactory(config);
        userRepository = new JdbcUserRepository(connectionFactory);
        articleRepository = new JdbcArticleRepository(connectionFactory);
    }

    @AfterEach
    void clearTables() throws SQLException {
        try (
                var connection = connectionFactory.openConnection();
                var statement = connection.createStatement()
        ) {
            statement.execute("TRUNCATE articles, users RESTART IDENTITY CASCADE");
        }
    }
}
