package com.epam.esm.exceptions;

public class UserNotFoundException extends ApiException {


    public UserNotFoundException( long id) {
        super("(id=" + id + ")");
    }

    public UserNotFoundException( String username) {
        super("(username=" + username + ")");
    }

}
