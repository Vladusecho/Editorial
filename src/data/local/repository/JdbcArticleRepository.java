package data.local.repository;

import com.fasterxml.jackson.databind.ext.SqlBlobSerializer;
import data.local.database.DatabaseConnectionFactory;
import domain.model.Article;
import domain.repository.ArticleRepository;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcArticleRepository implements ArticleRepository {
    private final DatabaseConnectionFactory connectionFactory;

    public JdbcArticleRepository(DatabaseConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void addArticle(Article article) {
        String sql = """
                        INSERT INTO articles (author_id, status_id, title, content, published_at)
                        VALUES (?, (SELECT id FROM article_statuses WHERE code = ?), ?, ?, ?::timestamptz)
                        RETURNING id
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setInt(1, article.getAuthorId());
            statement.setString(2, article.getStatus().name());
            statement.setString(3, article.getTitle());
            statement.setString(4, article.getContent());
            statement.setString(5, article.getPublishedAt());

            try (var result = statement.executeQuery()) {
                if (!result.next()) throw new IllegalStateException("Database didn't return anything");

                article.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't add article", e);
        }
    }

    @Override
    public void deleteArticle(int articleId) {
        String sql = """
                DELETE FROM articles WHERE id = ?
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setInt(1, articleId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) throw new IllegalArgumentException("No article found");


        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't delete article");
        }
    }

    @Override
    public void editArticle(Article article) {
        String sql = """
                UPDATE articles
                SET
                    status_id = (SELECT id FROM article_statuses WHERE code = ?),
                    title = ?,
                    content = ?
                WHERE id = ?
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setString(1, article.getStatus().name());
            statement.setString(2, article.getTitle());
            statement.setString(3, article.getContent());
            statement.setInt(4, article.getId());

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) throw new IllegalArgumentException("Couldn't find article");

        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't edit article");
        }
    }

    @Override
    public void filterArticles() {

    }

    @Override
    public Article getArticleById(int articleId) {
        String sql = """
                SELECT
                    article.id,
                    article.author_id,
                    article.title,
                    article.content,
                    status.code AS status_code,
                    article.published_at
                FROM articles AS article
                JOIN article_statuses AS status ON status.id = article.status_id
                WHERE article.id = ?;
                """;

        try (var connection = connectionFactory.openConnection(); var statement = connection.prepareStatement(sql);) {
            statement.setInt(1, articleId);

            try (var result = statement.executeQuery()) {
                if (!result.next()) throw new IllegalArgumentException("Couldn't find an article");

                Article returnArticle = new Article(result.getInt("id"), result.getInt("author_id"), result.getString("title"), result.getString("content"), Article.Status.valueOf(result.getString("status_code")), result.getString("published_at"));

                return returnArticle;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Couldn't get the article by id");
        }

    }

    @Override
    public List<Article> getArticles() {
        String sql = """
                SELECT * FROM articles;
                """;

        try (var connection = connectionFactory.openConnection();
             var statement = connection.prepareStatement(sql);
        ) {

            try (var resSet = statement.executeQuery()) {
                List<Article> returnArticles = new ArrayList<Article>();

                while (resSet.next()) {
                    returnArticles.add(getArticleById(resSet.getInt("id")));
                }

                return returnArticles;
            }

        }
        catch (SQLException e) {
            throw new IllegalStateException("Couldn't get articles", e);
        }

    }

    @Override
    public List<Article> searchArticle(String keyword) {
        return List.of();
    }

    @Override
    public List<Article> sortArticles() {
        return List.of();
    }
}
