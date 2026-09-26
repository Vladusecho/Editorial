package domain.usecase;

import domain.model.Article;
import domain.repository.ArticleRepository;

import java.util.List;

public class GetArticlesUseCase {
    private final ArticleRepository articleRepository;

    public GetArticlesUseCase(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> execute() {
        return articleRepository.getArticles();
    }
}
