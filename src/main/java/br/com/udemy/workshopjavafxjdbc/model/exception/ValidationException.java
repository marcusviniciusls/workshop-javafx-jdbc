package br.com.udemy.workshopjavafxjdbc.model.exception;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException{

    private Map<String, String> errors = new HashMap<>();

    public ValidationException(String message) {
        super(message);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String fieldName, String errorMessage){
        this.errors.put(fieldName, errorMessage);
    }
}
