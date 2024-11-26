package com.example.hands_on_java_springboot_jdbc.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ValidationErrorException extends Exception {
    final String message;

    public ValidationErrorException(String message) {
        this.message = message;
    }
}
