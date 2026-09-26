package presentation;

import domain.model.Article;
import domain.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsoleViewTest {
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();

    private Presenter presenter;
    private ConsoleView view;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        presenter = mock(Presenter.class);
        view = new ConsoleView(null);
        view.setPresenter(presenter);
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }

    @Test
    void showStatsPrintsUserAndArticleCountsGroupedByStatus() {
        when(presenter.onGetUsers()).thenReturn(List.of(
                user(1),
                user(2)
        ));
        when(presenter.onGetArticles()).thenReturn(List.of(
                article(1, Article.Status.PENDING),
                article(2, Article.Status.MODERATING),
                article(3, Article.Status.PUBLISHED),
                article(4, Article.Status.REJECTED)
        ));

        view.showStats();

        String result = output.toString(StandardCharsets.UTF_8);
        assertAll(
                () -> assertTrue(result.contains("User count: 2")),
                () -> assertTrue(result.contains("Article count: 4")),
                () -> assertTrue(result.contains("Articles being moderated: 1")),
                () -> assertTrue(result.contains("Articles publised: 1")),
                () -> assertTrue(result.contains("Articles rejected: 1"))
        );
        verify(presenter).onGetUsers();
        verify(presenter).onGetArticles();
    }

    private static User user(int id) {
        return new User(id, "user" + id, "user" + id + "@example.com", "hash", User.Role.AUTHOR);
    }

    private static Article article(int id, Article.Status status) {
        return new Article(id, 1, "Title", "Content", status, null);
    }
}
