package com.epam.esm.exceptions;

public class CertificateNotFoundException extends ApiException {


    public CertificateNotFoundException(long id) {
        super( "(id=" + id + ")");
    }

}
