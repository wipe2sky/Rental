package com.kurtsevich.rental.api.dao;

import com.kurtsevich.rental.model.BaseEntity;

import java.util.List;

public interface GenericDao<T extends BaseEntity> {

    T getById(Long id);

    List<T> getAll();

    void save(T entity);

    void delete(T entity);

    void update(T entity);

}
