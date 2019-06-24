package ru.akudinov.test.service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akudinov.test.service.exception.BlockedPersonException;
import ru.akudinov.test.service.exception.TooManyRequestException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler 
  extends ResponseEntityExceptionHandler {
 
    @ExceptionHandler(value
      = { TooManyRequestException.class })
    protected ResponseEntity<Object> handleTooManyRequest(TooManyRequestException ex, WebRequest request) {
        return handleExceptionInternal(ex, "",
          new HttpHeaders(), HttpStatus.TOO_MANY_REQUESTS, request);
    }

    @ExceptionHandler(value
            = { BlockedPersonException.class })
    protected ResponseEntity<Object> handleBlockedPerson(BlockedPersonException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Blocked person",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value
            = { Exception.class })
    protected ResponseEntity<Object> handleUnexpected(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "",
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


}