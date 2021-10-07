package com.epam.esm.handler;


import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagAlreadyAssociatedException;
import com.epam.esm.exceptions.TagNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;
    private final static String CERTIFICATE_NOT_FOUND_ERROR_CODE = "40401";
    private final static String TAG_NOT_FOUND_ERROR_CODE = "40402";
    private final static String TAG_ASSOCIATED_ERROR_CODE = "40901";

    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({CertificateNotFoundException.class})
    public ErrorInfo handleCertificateNotFoundException(CertificateNotFoundException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    @ExceptionHandler({TagNotFoundException.class})
    public ErrorInfo handleTagNotFoundException(TagNotFoundException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }

    @ExceptionHandler({TagAlreadyAssociatedException.class})
    public ErrorInfo handleTagNotFoundException(TagAlreadyAssociatedException exception, Locale locale) {
        return new ErrorInfo(
                HttpStatus.CONFLICT,
                TAG_ASSOCIATED_ERROR_CODE,
                messageSource.getMessage(exception.getMessage(), new Object[]{exception.getAttribute()}, locale)
        );
    }


}
