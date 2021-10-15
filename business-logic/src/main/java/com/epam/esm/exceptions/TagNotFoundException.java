package com.epam.esm.exceptions;

public class TagNotFoundException extends ApiException {


    public TagNotFoundException( long id) {
        super("(id=" + id + ")");
    }

}
