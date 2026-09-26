package domain.usecase;

import domain.model.Article;
import domain.repository.ArticleRepository;
import domain.validator.ArticleValidator;
import domain.validator.IdValidator;

public class EditArticleUseCase {
    private final ArticleRepository articleRepository;
    private final ArticleValidator articleValidator;
    private final IdValidator idValidator;

    public EditArticleUseCase(
            ArticleRepository articleRepository,
            ArticleValidator articleValidator,
            IdValidator idValidator
    ) {
        this.articleRepository = articleRepository;
        this.articleValidator = articleValidator;
        this.idValidator = idValidator;
    }

    public void execute(int articleId, String title, String content, Article.Status status) {
        idValidator.validate(articleId, "Article ID"); // Валидируем id

        Article article = articleRepository.getArticleById(articleId); // получаем статью

        // Изменяем статью
        article.setTitle(title);
        article.setContent(content);
        article.setStatus(status);

        articleValidator.validate(article); // Валидируем статью
        articleRepository.editArticle(article); // Редактируем в БД
    }
}
