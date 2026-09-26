package application;

import data.local.database.DatabaseConfig;
import data.local.database.DatabaseConnectionFactory;
import data.local.database.DatabaseMigrator;
import data.local.repository.JdbcArticleRepository;
import data.local.repository.JdbcUserRepository;
import domain.repository.ArticleRepository;
import domain.repository.UserRepository;
import domain.usecase.*;
import domain.validator.ArticleValidator;
import domain.validator.IdValidator;
import domain.validator.UserValidator;
import presentation.ConsoleView;
import presentation.Presenter;
import presentation.validation.InputValidationService;

public class Application {
    public static void main(String[] args) {
        DatabaseConfig config = new DatabaseConfig();
        DatabaseMigrator migrator = new DatabaseMigrator(config);
        DatabaseConnectionFactory connectionFactory = new DatabaseConnectionFactory(config);
        ArticleRepository articleRepository = new JdbcArticleRepository(connectionFactory);
        UserRepository userRepository = new JdbcUserRepository(connectionFactory);

        ArticleValidator articleValidator = new ArticleValidator(userRepository);
        UserValidator userValidator = new UserValidator();
        IdValidator idValidator = new IdValidator();
        InputValidationService inputValidationService = new InputValidationService(
                idValidator,
                articleValidator,
                userValidator
        );

        ConsoleView view = new ConsoleView(inputValidationService);
        Presenter presenter = new Presenter(
                view,
                new GetArticlesUseCase(articleRepository),
                new AddArticleUseCase(articleRepository, articleValidator),
                new EditArticleUseCase(articleRepository, articleValidator, idValidator),
                new GetArticleByIdUseCase(articleRepository, idValidator),
                new DeleteArticleUseCase(articleRepository, idValidator),
                new FilterArticlesUseCase(),
                new SortArticlesUseCase(),
                new SearchArticleUseCase(),
                new AddUserUseCase(userRepository, userValidator),
                new EditUserUseCase(userRepository, userValidator, idValidator),
                new DeleteUserUseCase(userRepository, idValidator),
                new GetUserByIdUseCase(userRepository, idValidator),
                new GetUsersUseCase(userRepository)
        );
        view.setPresenter(presenter);

        migrator.migrate();
        view.run();
    }
}
