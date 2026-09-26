package presentation;

import domain.model.Article;
import domain.model.User;

import java.util.List;

public interface View {
    void showStartOptions();

    void showArticle(Article article);

    void showArticles();

    void showUser(User user);

    void showUsers();

    void showMessage(String message);

    void showError(String error);

    String getUserInput(String prompt);

    int getMenuChoice();
}
