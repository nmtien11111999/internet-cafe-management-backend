package com.example.controller;

import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class ExceptionController {
    public static List<String> getMessageError(BindingResult bindingResult) {
        List<String> messages = new ArrayList<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            messages.add(error.getField()+":"+error.getDefaultMessage());
        }
        return messages;
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNullPointerException(IllegalArgumentException ex) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<String>> handleValidationException(ValidationException ex) {
        String message = ex.getLocalizedMessage();
        String[] errorsArray = message.split(";\\s*");
        List<String> result = Arrays.asList(errorsArray);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
