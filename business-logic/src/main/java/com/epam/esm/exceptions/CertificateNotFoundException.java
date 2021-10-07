package com.epam.esm.exceptions;



public class CertificateNotFoundException extends ApiException {


    public CertificateNotFoundException(String message, long id) {
        super(message);
        setAttribute("(id=" + id + ")");
    }

    public CertificateNotFoundException(String message) {
        super(message);
    }
}
