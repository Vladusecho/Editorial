package domain.usecase;

import domain.model.Article;
import domain.repository.ArticleRepository;
import domain.validator.IdValidator;

public class DeleteArticleUseCase {
    private final ArticleRepository articleRepository;
    private final IdValidator idValidator;

    public DeleteArticleUseCase(ArticleRepository articleRepository, IdValidator idValidator) {
        this.articleRepository = articleRepository;
        this.idValidator = idValidator;
    }

    public void execute(int articleId) {
        idValidator.validate(articleId, "Article ID"); // Валидируем
        articleRepository.deleteArticle(articleId); // Удаляем
    }
}
