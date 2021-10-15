package com.epam.esm.exceptions;


public class TagAlreadyAssociatedException extends ApiException {


    public TagAlreadyAssociatedException(long certificateId, long tagId) {
        super( "(certificateId=" + certificateId + ", tagId=" + tagId + ")");
    }
}
