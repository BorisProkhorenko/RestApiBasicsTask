package com.epam.esm.exceptions;

public class TagNotFoundException extends ApiException {


    public TagNotFoundException(String message, long id) {
        super(message);
        setAttribute("(id=" + id + ")");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
