package com.epam.esm.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class TagAlreadyAssociatedException extends ApiException {


    public TagAlreadyAssociatedException(String message, long certificateId, long tagId) {
        super(message);
        setAttribute("(certificateId=" + certificateId + ", tagId=" + tagId + ")");
    }

    public TagAlreadyAssociatedException(String message) {
        super(message);
    }
}
