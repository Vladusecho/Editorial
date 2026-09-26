package presentation;

import domain.model.Article;
import domain.model.User;
import domain.usecase.*;

import java.util.ArrayList;
import java.util.List;

// Класс Presenter ("мозги" UI): связывает View и UseCase-ы бизнес-логики
public class Presenter {
    // Ссылка на интерфейс отображения (View)
    private final View view;

    // UseCase-ы для работы со статьями
    private final GetArticlesUseCase getArticlesUseCase;
    private final AddArticleUseCase addArticleUseCase;
    private final GetArticleByIdUseCase getArticleByIdUseCase;
    private final DeleteArticleUseCase deleteArticleUseCase;
    private final FilterArticlesUseCase filterArticlesUseCase;
    private final EditArticleUseCase editArticleUseCase;
    private final SortArticlesUseCase sortArticlesUseCase;
    private final SearchArticleUseCase searchArticleUseCase;

    // UseCase-ы для работы с пользователями
    private final AddUserUseCase addUserUseCase;
    private final EditUserUseCase editUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final GetUsersUseCase getUsersUseCase;

    // Конструктор класса: принимает View и все UseCase-ы
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

    // Обработка добавления статьи
    public boolean onAddArticle(Article article) {
        try {
            addArticleUseCase.execute(article); // Вызываем юзкейс добавления
            view.showMessage("Article added");  // Выводим сообщение об успехе
            return true;
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());     // Если ошибка валидации или базы выводим ошибку
            return false;
        }
    }

    // Обработка получения всех статей
    public List<Article> onGetArticles() {
        List<Article> returnArticles = new ArrayList<Article>();
        try {
            returnArticles = getArticlesUseCase.execute();
        } catch (IllegalStateException e) {
            view.showError(e.getMessage());
        }
        return returnArticles;
    }

    // Обработка удаления статьи по id
    public void onDeleteArticle(int articleId) {
        try {
            deleteArticleUseCase.execute(articleId); // Вызываем юзкейс удаления по id
            view.showMessage("Article deleted");    // Выводим сообщение об успехе
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());         // Если не удалось удалить выводим ошибку
        }
    }

    // Обработка получения статьи по id
    public Article onGetArticleById(int articleId) {
        Article returnArticle = null;
        try {
            returnArticle = getArticleByIdUseCase.execute(articleId); // Получаем статью через юзкейс
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());                           // Если не нашли или ошибка выводим ошибку
        }

        return returnArticle; // Возвращаем статью
    }

    // Обработка фильтрации статей
    public void onFilterArticles() {
    }

    // Обработка сортировки статей
    public void onSortArticles() {
    }

    // Обработка поиска статей
    public void onSearchArticle() {
    }

    // Обработка редактирования статьи по id
    public void onEditArticle(int articleId, String title, String content, Article.Status status) {
        try {
            editArticleUseCase.execute(articleId, title, content, status); // Вызываем юзкейс редактирования
        } catch (IllegalStateException | IllegalArgumentException e) {
            view.showError(e.getMessage());                                // Если ошибка валидации или не найдено выводим ошибку
        }
    }

    // Обработка добавления пользователя
    public void onAddUser(User user) {
        try {
            addUserUseCase.execute(user);     // Вызываем юзкейс добавления пользователя
            view.showMessage("User added");   // Выводим сообщение об успехе
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());   // Если ошибка валидации или базы выводим ошибку
        }
    }

    // Обработка редактирования пользователя по id
    public void onEditUser(
            int userId,
            String username,
            String email,
            String passwordHash,
            User.Role role
    ) {
        try {
            editUserUseCase.execute(userId, username, email, passwordHash, role); // Вызываем юзкейс редактирования пользователя
            view.showMessage("User edited");                                      // Выводим сообщение об успехе
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());                                       // Если ошибка валидации или не найден выводим ошибку
        }
    }

    // Обработка удаления пользователя по id
    public void onDeleteUser(int userId) {
        try {
            deleteUserUseCase.execute(userId); // Вызываем юзкейс удаления пользователя
            view.showMessage("User deleted");  // Выводим сообщение об успехе
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());    // Если не удалось удалить выводим ошибку
        }
    }

    // Обработка получения пользователя по id
    public User onGetUserById(int userId) {
        User returnUser = null;

        try {
            returnUser = getUserByIdUseCase.execute(userId); // Получаем пользователя через юзкейс
        } catch (IllegalArgumentException | IllegalStateException e) {
            view.showError(e.getMessage());                  // Если не найден или ошибка выводим ошибку
        }

        return returnUser; // Возвращаем пользователя
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
