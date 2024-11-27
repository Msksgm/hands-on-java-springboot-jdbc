package com.example.hands_on_java_springboot_jdbc.util;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ValidationErrorExceptions extends ValidationErrorException {
    private final ArrayList<ValidationErrorException> validationErrorExceptionList;

    public ValidationErrorExceptions(String message, ArrayList<ValidationErrorException> validationErrorExceptionList) {
        super(message);
        this.validationErrorExceptionList = validationErrorExceptionList;
    }
}
