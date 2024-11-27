package com.example.hands_on_java_springboot_jdbc.domain;

import com.example.hands_on_java_springboot_jdbc.util.ValidationErrorException;
import com.example.hands_on_java_springboot_jdbc.util.ValidationErrorExceptions;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class CreatedArticle {
    private final Slug slug;
    private final Title title;
    private final Body body;

    private CreatedArticle(Slug slug, Title title, Body body) {
        this.slug = slug;
        this.title = title;
        this.body = body;
    }

    public static CreatedArticle newCreatedArticle(String title, String body) throws CreationErrorException {
        ArrayList<ValidationErrorException> errors = new ArrayList<>();

        Slug slug = Slug.newSlug();

        Title validatdTitle = null;
        try {
            validatdTitle = Title.newTitle(title);
        } catch (Title.CreationErrorException e) {
            errors.add(e);
        }

        Body validatedBody = null;
        try {
            validatedBody = Body.newBody(body);
        } catch (Body.CreationErrorException e) {
            errors.add(e);
        }

        if (!errors.isEmpty()) {
            throw new InvalidCreatedArticlePropertyError(new ValidationErrorExceptions("記事の作成に失敗しました", errors)) ;
        }

        return new CreatedArticle(slug, validatdTitle, validatedBody);
    }

    public static CreatedArticle newWithoutValidation(Slug slug, Title title, Body body) {
        return new CreatedArticle(slug, title, body);
    }

    /**
     * エンティティは識別子によって等価性を判断する
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CreatedArticle)) {
            return false;
        }

        return this.slug.equals(((CreatedArticle)obj).slug);
    }

    @Override
    public int hashCode() {
        return slug.hashCode() * 31;
    }

    public static sealed abstract class CreationErrorException extends ValidationErrorException permits InvalidCreatedArticlePropertyError {
        public CreationErrorException(String message) {
            super(message);
        }
    }

    @Getter
    public static final class InvalidCreatedArticlePropertyError extends CreationErrorException {
        private final ValidationErrorExceptions validationErrorExceptions;

        public InvalidCreatedArticlePropertyError(ValidationErrorExceptions validationErrorExceptions) {
            super(validationErrorExceptions.getMessage());
            this.validationErrorExceptions = validationErrorExceptions;
        }
    }
}
