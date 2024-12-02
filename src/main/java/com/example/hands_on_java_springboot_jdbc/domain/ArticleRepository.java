package com.example.hands_on_java_springboot_jdbc.domain;

public interface ArticleRepository {
    public CreatedArticle findBySlug(Slug slug) throws FindBySlugException;

    public static sealed abstract class FindBySlugException extends Exception permits ArticleNotFound {
        public FindBySlugException(String message) {
            super(message);
        }
    }

    public static final class ArticleNotFound extends FindBySlugException {
        public ArticleNotFound(Slug slug) {
            super("Article not found: " + slug);
        }
    }
}
