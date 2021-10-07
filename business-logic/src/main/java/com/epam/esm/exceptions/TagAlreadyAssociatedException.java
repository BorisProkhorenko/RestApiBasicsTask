package com.epam.esm.exceptions;


public class TagAlreadyAssociatedException extends ApiException {


    public TagAlreadyAssociatedException(String message, long certificateId, long tagId) {
        super(message + "(certificateId=" + certificateId + ", tagId=" + tagId + ")");
    }

    public TagAlreadyAssociatedException(String message) {
        super(message);
    }
}
