package domain.usecase;

import domain.model.Article;
import domain.repository.ArticleRepository;
import domain.validator.ArticleValidator;

public class AddArticleUseCase {
    private final ArticleRepository repository;
    private final ArticleValidator articleValidator;

    public AddArticleUseCase(ArticleRepository repository, ArticleValidator articleValidator) {
        this.repository = repository;
        this.articleValidator = articleValidator;
    }

    public void execute(Article article) {
        articleValidator.validate(article); // Валидируем
        repository.addArticle(article); // Добавляем
    }
}
