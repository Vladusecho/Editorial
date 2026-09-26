package presentation;

import domain.model.Article;
import domain.model.User;
import domain.usecase.AddArticleUseCase;
import domain.usecase.AddUserUseCase;
import domain.usecase.DeleteArticleUseCase;
import domain.usecase.DeleteUserUseCase;
import domain.usecase.EditArticleUseCase;
import domain.usecase.EditUserUseCase;
import domain.usecase.FilterArticlesUseCase;
import domain.usecase.GetArticleByIdUseCase;
import domain.usecase.GetArticlesUseCase;
import domain.usecase.GetUserByIdUseCase;
import domain.usecase.GetUsersUseCase;
import domain.usecase.SearchArticleUseCase;
import domain.usecase.SortArticlesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresenterTest {
    private final RecordingView view = new RecordingView();
    private final StubAddArticleUseCase addArticleUseCase = new StubAddArticleUseCase();
    private final StubGetArticleByIdUseCase getArticleByIdUseCase = new StubGetArticleByIdUseCase();
    private final StubDeleteArticleUseCase deleteArticleUseCase = new StubDeleteArticleUseCase();
    private final StubAddUserUseCase addUserUseCase = new StubAddUserUseCase();
    private final StubEditUserUseCase editUserUseCase = new StubEditUserUseCase();
    private final StubDeleteUserUseCase deleteUserUseCase = new StubDeleteUserUseCase();
    private final StubGetUserByIdUseCase getUserByIdUseCase = new StubGetUserByIdUseCase();

    private Presenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new Presenter(
                view,
                new GetArticlesUseCase(null),
                addArticleUseCase,
                new EditArticleUseCase(null, null, null),
                getArticleByIdUseCase,
                deleteArticleUseCase,
                new FilterArticlesUseCase(),
                new SortArticlesUseCase(),
                new SearchArticleUseCase(),
                addUserUseCase,
                editUserUseCase,
                deleteUserUseCase,
                getUserByIdUseCase,
                new GetUsersUseCase(null)
        );
    }

    @Test
    void onAddArticleShowsSuccess() {
        Article article = article();

        assertTrue(presenter.onAddArticle(article));
        assertSame(article, addArticleUseCase.lastArticle);
        assertEquals(List.of("Article added"), view.messages);
    }

    @Test
    void onAddArticleShowsErrorAndReturnsFalse() {
        addArticleUseCase.error = new IllegalArgumentException("Author with ID 7 does not exist");

        assertFalse(presenter.onAddArticle(article()));
        assertEquals(List.of("Author with ID 7 does not exist"), view.errors);
    }

    @Test
    void onGetArticleByIdReturnsArticle() {
        Article article = article();
        getArticleByIdUseCase.result = article;

        assertSame(article, presenter.onGetArticleById(1));
        assertEquals(1, getArticleByIdUseCase.lastId);
    }

    @Test
    void onGetArticleByIdShowsErrorAndReturnsNull() {
        getArticleByIdUseCase.error = new IllegalArgumentException("No article found");

        assertNull(presenter.onGetArticleById(1));
        assertEquals(List.of("No article found"), view.errors);
    }

    @Test
    void onDeleteArticleShowsSuccess() {
        presenter.onDeleteArticle(1);

        assertEquals(1, deleteArticleUseCase.lastId);
        assertEquals(List.of("Article deleted"), view.messages);
    }

    @Test
    void onAddUserShowsErrorFromUseCase() {
        addUserUseCase.error = new IllegalArgumentException("Email is invalid");

        presenter.onAddUser(user());

        assertEquals(List.of("Email is invalid"), view.errors);
    }

    @Test
    void onGetUserByIdReturnsUser() {
        User user = user();
        getUserByIdUseCase.result = user;

        assertSame(user, presenter.onGetUserById(2));
        assertEquals(2, getUserByIdUseCase.lastId);
    }

    @Test
    void onEditUserShowsSuccess() {
        presenter.onEditUser(2, "bob", "bob@example.com", "hash", User.Role.EDITOR);

        assertEquals(2, editUserUseCase.userId);
        assertEquals("bob", editUserUseCase.username);
        assertEquals("bob@example.com", editUserUseCase.email);
        assertEquals("hash", editUserUseCase.passwordHash);
        assertEquals(User.Role.EDITOR, editUserUseCase.role);
        assertEquals(List.of("User edited"), view.messages);
    }

    @Test
    void onDeleteUserShowsSuccess() {
        presenter.onDeleteUser(2);

        assertEquals(2, deleteUserUseCase.lastId);
        assertEquals(List.of("User deleted"), view.messages);
    }

    private static Article article() {
        return new Article(1, 7, "Title", "Long enough", Article.Status.PENDING, null);
    }

    private static User user() {
        return new User(2, "alice", "alice@example.com", "hash", User.Role.AUTHOR);
    }

    private static final class RecordingView implements View {
        private final List<String> messages = new ArrayList<>();
        private final List<String> errors = new ArrayList<>();

        @Override
        public void showStartOptions() {
        }

        @Override
        public void showArticle(Article article) {
        }

        @Override
        public void showArticles() {
        }

        @Override
        public void showUser(User user) {
        }

        @Override
        public void showUsers() {
        }

        @Override
        public void showMessage(String message) {
            messages.add(message);
        }

        @Override
        public void showError(String error) {
            errors.add(error);
        }

        @Override
        public String getUserInput(String prompt) {
            return "";
        }

        @Override
        public int getMenuChoice() {
            return 0;
        }
    }

    private static final class StubAddArticleUseCase extends AddArticleUseCase {
        private Article lastArticle;
        private RuntimeException error;

        private StubAddArticleUseCase() {
            super(null, null);
        }

        @Override
        public void execute(Article article) {
            lastArticle = article;
            if (error != null) {
                throw error;
            }
        }
    }

    private static final class StubGetArticleByIdUseCase extends GetArticleByIdUseCase {
        private int lastId;
        private Article result;
        private RuntimeException error;

        private StubGetArticleByIdUseCase() {
            super(null, null);
        }

        @Override
        public Article execute(int articleId) {
            lastId = articleId;
            if (error != null) {
                throw error;
            }
            return result;
        }
    }

    private static final class StubDeleteArticleUseCase extends DeleteArticleUseCase {
        private int lastId;

        private StubDeleteArticleUseCase() {
            super(null, null);
        }

        @Override
        public void execute(int articleId) {
            lastId = articleId;
        }
    }

    private static final class StubAddUserUseCase extends AddUserUseCase {
        private RuntimeException error;

        private StubAddUserUseCase() {
            super(null, null);
        }

        @Override
        public void execute(User user) {
            if (error != null) {
                throw error;
            }
        }
    }

    private static final class StubEditUserUseCase extends EditUserUseCase {
        private int userId;
        private String username;
        private String email;
        private String passwordHash;
        private User.Role role;

        private StubEditUserUseCase() {
            super(null, null, null);
        }

        @Override
        public void execute(int userId, String username, String email, String passwordHash, User.Role role) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.passwordHash = passwordHash;
            this.role = role;
        }
    }

    private static final class StubDeleteUserUseCase extends DeleteUserUseCase {
        private int lastId;

        private StubDeleteUserUseCase() {
            super(null, null);
        }

        @Override
        public void execute(int userId) {
            lastId = userId;
        }
    }

    private static final class StubGetUserByIdUseCase extends GetUserByIdUseCase {
        private int lastId;
        private User result;

        private StubGetUserByIdUseCase() {
            super(null, null);
        }

        @Override
        public User execute(int userId) {
            lastId = userId;
            return result;
        }
    }
}
