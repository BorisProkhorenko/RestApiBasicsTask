package com.epam.esm.handler;


import com.epam.esm.exceptions.CertificateNotFoundException;
import com.epam.esm.exceptions.InvalidRequestException;
import com.epam.esm.exceptions.TagNotFoundException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;


@RestControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private final static String CERTIFICATE_NOT_FOUND_ERROR_CODE = "40401";
    private final static String TAG_NOT_FOUND_ERROR_CODE = "40402";
    private final static String INVALID_REQUEST_ERROR_CODE = "40000";

    private final static String MESSAGE_CERTIFICATE_NOT_FOUND = "message.certificate_not_found";
    private final static String MESSAGE_TAG_NOT_FOUND = "message.tag_not_found";
    private final static String MESSAGE_INVALID_REQUEST = "message.invalid_request";

    private final MessageSource messageSource;

    public RestResponseEntityExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler({CertificateNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleCertificateNotFoundException(CertificateNotFoundException exception,
                                                                        Locale locale) {
        ErrorInfo info = new ErrorInfo(
                CERTIFICATE_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(
                        MESSAGE_CERTIFICATE_NOT_FOUND, new Object[]{exception.getMessage()}, locale)
        );
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({TagNotFoundException.class})
    public ResponseEntity<ErrorInfo> handleTagNotFoundException(TagNotFoundException exception, Locale locale) {
        ErrorInfo info = new ErrorInfo(
                TAG_NOT_FOUND_ERROR_CODE,
                messageSource.getMessage(
                        MESSAGE_TAG_NOT_FOUND, new Object[]{exception.getMessage()}, locale)
        );
        return new ResponseEntity<>(info, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException exception,
                                                                   Locale locale) {
        return handleRequestExceptions(exception, HttpStatus.BAD_REQUEST, locale);
    }

    private ResponseEntity<Object> handleRequestExceptions(Exception ex, HttpStatus status, Locale locale) {
        ErrorInfo info = new ErrorInfo(
                INVALID_REQUEST_ERROR_CODE,
                messageSource.getMessage(
                        MESSAGE_INVALID_REQUEST, new Object[]{"(" + ex.getMessage() + ")"}, locale)
        );
        return new ResponseEntity<>(info, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex,
                                                                      HttpHeaders headers, HttpStatus status,
                                                                      WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        return handleRequestExceptions(ex, status, webRequest.getLocale());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleRequestExceptions(ex, status, request.getLocale());
    }
}
