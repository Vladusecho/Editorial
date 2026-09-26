package data.local.database;

import org.flywaydb.core.Flyway;

public class DatabaseMigrator {
    private final DatabaseConfig databaseConfig;

    public DatabaseMigrator(DatabaseConfig config) {
        this.databaseConfig = config;
    }

    public void migrate() {
        Flyway flyway = Flyway.configure().dataSource(databaseConfig.getUrl(), databaseConfig.getUser(), databaseConfig.getPassword()) // Инициализация и передача данных конфига
                .locations("classpath:db/migration") // Место с файлами миграции
                .load(); // Сборка готового Flyway

        // Выполнение всех миграций
        flyway.migrate();
    }
}
