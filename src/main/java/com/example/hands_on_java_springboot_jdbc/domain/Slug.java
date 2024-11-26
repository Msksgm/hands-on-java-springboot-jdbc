package com.example.hands_on_java_springboot_jdbc.domain;

import com.example.hands_on_java_springboot_jdbc.util.ValidationErrorException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public final class Slug {
    private final String value;

    private Slug(String value) {
        this.value = value;
    }

    static public Slug newSlug() {
        return new Slug(String.join("", UUID.randomUUID().toString().split("-")));
    }

    static public Slug newSlug(String slug) throws CreationErrorException {
        final String slugFormat = "^[a-z0-9]{32}$";

        if (slug == null) {
            throw new EmptySlugError();
        }

        if (!slug.matches(slugFormat)) {
            throw new InvalidSlugFormatError(slug);
        }

        return new Slug(slug);
    }

    static public Slug newWithoutValidation(String slug) {
        return new Slug(slug);
    }

    public static sealed abstract class CreationErrorException extends ValidationErrorException permits EmptySlugError, InvalidSlugFormatError {
        public CreationErrorException(String message) {
            super(message);
        }
    }

    public static final class EmptySlugError extends CreationErrorException {
        public EmptySlugError() {
            super("Slugは必須です");
        }
    }

    public static final class InvalidSlugFormatError extends CreationErrorException {
        public InvalidSlugFormatError(String slug) {
            super(String.format("slugのフォーマットが不正です:%s", slug));
        }
    }
}
