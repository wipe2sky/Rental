package com.kurtsevich.rental.api.exception;

public class NotFoundEntityException extends RuntimeException{
    public NotFoundEntityException(Long id){
        super("Couldn't find entity by id " + id);
    }

    public NotFoundEntityException(String name){
        super("Couldn't find entity " + name);
    }
}
