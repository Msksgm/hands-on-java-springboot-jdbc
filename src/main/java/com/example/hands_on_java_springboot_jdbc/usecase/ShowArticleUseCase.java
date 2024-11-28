package com.example.hands_on_java_springboot_jdbc.usecase;

import com.example.hands_on_java_springboot_jdbc.domain.CreatedArticle;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface ShowArticleUseCase {
    public CreatedArticle execute(String slug) throws ShowArticleUseCaseException;

    sealed abstract class ShowArticleUseCaseException extends Exception permits CreatedArticleValidationErrors, NotFoundArticleBySlug {
        public ShowArticleUseCaseException(String message) {
        }
    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    static final class CreatedArticleValidationErrors extends ShowArticleUseCaseException {
        private final CreatedArticleValidationErrors validationErrors;

        public CreatedArticleValidationErrors(CreatedArticleValidationErrors validationError, String message) {
            super(message);
            this.validationErrors = validationError;
        }
    }

    static final class NotFoundArticleBySlug extends ShowArticleUseCaseException {
        public NotFoundArticleBySlug(String message) {
            super(message);
        }
    }
}
