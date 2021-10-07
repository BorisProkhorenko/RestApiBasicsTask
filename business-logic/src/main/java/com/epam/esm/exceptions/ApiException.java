package com.epam.esm.exceptions;

public class ApiException extends RuntimeException{

    private String attribute;

    public ApiException(String message, String attribute) {
        super(message);
        this.attribute = attribute;
    }

    public ApiException(String message) {
        super(message);
        this.attribute = "";
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}


