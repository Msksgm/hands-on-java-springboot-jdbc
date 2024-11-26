package com.example.hands_on_java_springboot_jdbc.domain;

import com.example.hands_on_java_springboot_jdbc.util.ValidationErrorException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
final public class Body {
    private final String value;

    private Body(String value) {
        this.value = value;
    }

    static public Body newBody(String body) throws CreationErrorException {
        final int maxBodyLength = 1024;

        if (body == null || body.isEmpty()) {
            throw new EmptyBodyError();
        }

        if (body.length() > maxBodyLength) {
            throw new TooLongBodyFormatError(maxBodyLength);
        }

        return new Body(body);
    }

    static public Body newWithoutValidation(String body) {
        return new Body(body);
    }

    public static sealed abstract class CreationErrorException extends ValidationErrorException permits EmptyBodyError, TooLongBodyFormatError {
        public CreationErrorException(String message) {
            super(message);
        }
    }

    public static final class EmptyBodyError extends CreationErrorException {
        public EmptyBodyError() {
            super("Bodyは必須です");
        }
    }

    public static final class TooLongBodyFormatError extends CreationErrorException {
        public TooLongBodyFormatError(int maxBodyLength) {
            super(String.format("Bodyは%d文字以下で入力してください", maxBodyLength));
        }
    }
}
