package com.kurtsevich.rental.controller;

import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.api.exception.ServiceException;
import com.kurtsevich.rental.dto.exception.ExceptionDto;
import com.kurtsevich.rental.dto.exception.ValidationExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundEntityException.class)
    private ResponseEntity<ExceptionDto> handleNotFoundEntityException(NotFoundEntityException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    private ResponseEntity<ExceptionDto> handleAuthenticationException(AuthenticationException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<ExceptionDto> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationExceptionDto exceptionDto = new ValidationExceptionDto();
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        exceptionDto.setException(ex.getClass().getSimpleName());
        exceptionDto.setMessages(details);
        log.error(details.toString());
        return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    private ResponseEntity<ExceptionDto> handleDataAccessException(DataAccessException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getCause().getCause().getLocalizedMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ServiceException.class)
    private ResponseEntity<ExceptionDto> handleServiceException(ServiceException ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    private ResponseEntity<ExceptionDto> handleUnexpectedException(Exception ex) {
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ResponseEntity<>(new ExceptionDto()
                .setException(ex.getClass().getSimpleName())
                .setMessage(ex.getLocalizedMessage())
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}