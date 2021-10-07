package com.epam.esm.handler;


import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.TagAlreadyAssociatedException;
import com.epam.esm.exceptions.TagNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Locale;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private final static String CERTIFICATE_NOT_FOUND_ERROR_CODE = "40401";
    private final static String TAG_NOT_FOUND_ERROR_CODE = "40402";
    private final static String TAG_ASSOCIATED_ERROR_CODE = "40901";


    @ExceptionHandler({CertificateNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleCertificateNotFoundException(CertificateNotFoundException exception) {
        ErrorInfo info = new ErrorInfo(
                HttpStatus.NOT_FOUND,
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                exception.getMessage()
        );
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TagNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleTagNotFoundException(TagNotFoundException exception) {
        ErrorInfo info =  new ErrorInfo(
                HttpStatus.NOT_FOUND,
                TAG_NOT_FOUND_ERROR_CODE,
                exception.getMessage()
        );
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TagAlreadyAssociatedException.class})
    public ResponseEntity<ErrorInfo> handleTagNotFoundException(TagAlreadyAssociatedException exception) {
        ErrorInfo info = new ErrorInfo(
                HttpStatus.CONFLICT,
                TAG_ASSOCIATED_ERROR_CODE,
                exception.getMessage()
        );
        return new ResponseEntity<>(info, HttpStatus.CONFLICT);
    }


}
