package presentation;

import domain.model.Article;
import domain.model.User;
import domain.usecase.*;

import java.util.ArrayList;
import java.util.List;

public class Presenter {
    private final View view;
    private final GetArticlesUseCase getArticlesUseCase;
    private final AddArticleUseCase addArticleUseCase;
    private final GetArticleByIdUseCase getArticleByIdUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;
    private final FilterArticlesUseCase filterArticlesUseCase;
    private final EditArticleUseCase editArticleUseCase;
    private final SortArticlesUseCase sortArticlesUseCase;
    private final SearchArticleUseCase searchArticleUseCase;
    private final AddUserUseCase addUserUseCase;
    private final EditUserUseCase editUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUsersUseCase getUsersUseCase;


    public Presenter(
            View view,
            GetArticlesUseCase getArticlesUseCase,
            AddArticleUseCase addArticleUseCase,
            EditArticleUseCase editArticleUseCase,
            GetArticleByIdUseCase getArticleByIdUseCase,
            DeleteArticleUseCase deleteArticleUseCase,
            FilterArticlesUseCase filterArticlesUseCase,
            SortArticlesUseCase sortArticlesUseCase,
            SearchArticleUseCase searchArticleUseCase,
            AddUserUseCase addUserUseCase,
            EditUserUseCase editUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            GetUserByIdUseCase getUserByIdUseCase,
            GetUsersUseCase getUsersUseCase

    ) {
        this.view = view;
        this.getArticlesUseCase = getArticlesUseCase;
        this.addArticleUseCase = addArticleUseCase;
        this.deleteArticleUseCase = deleteArticleUseCase;
        this.getArticleByIdUseCase = getArticleByIdUseCase;
        this.filterArticlesUseCase = filterArticlesUseCase;
        this.sortArticlesUseCase = sortArticlesUseCase;
        this.searchArticleUseCase = searchArticleUseCase;
        this.editArticleUseCase = editArticleUseCase;
        this.addUserUseCase = addUserUseCase;
        this.editUserUseCase = editUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.getUserByIdUseCase = getUserByIdUseCase;
        this.getUsersUseCase = getUsersUseCase;
    }

    public boolean onAddArticle(Article article) {
        try {
            addArticleUseCase.execute(article);
            view.showMessage("Article added");
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
            return false;
        }
    }

    public List<Article> onGetArticles() {
        List<Article> returnArticles = new ArrayList<Article>();
        try {
            returnArticles = getArticlesUseCase.execute();
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
        return returnArticles;
    }

    public void onDeleteArticle(int articleId) {
        try {
            deleteArticleUseCase.execute(articleId);
            view.showMessage("Article deleted");
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public Article onGetArticleById(int articleId) {
        Article returnArticle = null;
        try {
            returnArticle = getArticleByIdUseCase.execute(articleId);
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());
        }

        return returnArticle;
    }

    public void onFilterArticles() {
    }

    public void onSortArticles() {
    }

    public void onSearchArticle() {
    }

    public void onEditArticle(int articleId, String title, String content, Article.Status status) {
        try {
            editArticleUseCase.execute(articleId, title, content, status);
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());
        }
    }

    public void onAddUser(User user) {
        try {
            addUserUseCase.execute(user);
            view.showMessage("User added");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public void onEditUser(
            int userId,
            String username,
            String email,
            String passwordHash,
            User.Role role
    ) {
        try {
            editUserUseCase.execute(userId, username, email, passwordHash, role);
            view.showMessage("User edited");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public void onDeleteUser(int userId) {
        try {
            deleteUserUseCase.execute(userId);
            view.showMessage("User deleted");
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }
    }

    public User onGetUserById(int userId) {
        User returnUser = null;

        try {
            returnUser = getUserByIdUseCase.execute(userId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());
        }

        return returnUser;
    }

    public List<User> onGetUsers() {
        try {
            return getUsersUseCase.execute();
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
            return List.of();
        }
    }

}
