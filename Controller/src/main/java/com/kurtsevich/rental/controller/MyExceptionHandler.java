package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.exception.DaoException;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.security.sasl.AuthenticationException;

@ControllerAdvice
@Slf4j
public class MyExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(NotFoundEntityException.class)
    protected ResponseEntity<String> handleNotFoundEntityException(NotFoundEntityException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> handleNotFoundException(NotFoundEntityException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DaoException.class)
    protected ResponseEntity<String> handleDaoException(DaoException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        log.error("IN handleAuthenticationException - invalid username or password");
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleUnexpectedException(Exception ex) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
