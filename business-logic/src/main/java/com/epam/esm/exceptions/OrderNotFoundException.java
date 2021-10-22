package com.epam.esm.exceptions;

public class OrderNotFoundException extends ApiException {


    public OrderNotFoundException(long id) {
        super( "(id=" + id + ")");
    }

}

