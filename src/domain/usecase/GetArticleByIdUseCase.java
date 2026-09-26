package domain.usecase;

import domain.model.Article;
import domain.repository.ArticleRepository;
import domain.validator.IdValidator;

public class GetArticleByIdUseCase {
    private final ArticleRepository articleRepository;
    private final IdValidator idValidator;

    public GetArticleByIdUseCase(ArticleRepository articleRepository, IdValidator idValidator) {
        this.articleRepository = articleRepository;
        this.idValidator = idValidator;
    }

    public Article execute(int articleId) {
        idValidator.validate(articleId, "Article ID"); // Валидируем id
        return articleRepository.getArticleById(articleId); // возвращаем статью
    }
}
