package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.GenericDao;
import com.kurtsevich.rental.api.exception.DaoException;
import com.kurtsevich.rental.api.exception.NotFoundEntityException;
import com.kurtsevich.rental.model.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public abstract class AbstractDao<T extends BaseEntity> implements GenericDao<T> {
    @PersistenceContext
    protected EntityManager em;

    protected abstract Class<T> getClazz();

    @Override
    public void save(T entity) {
        try {
            em.persist(entity);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public T getById(Long id) {
        T entity = em.find(getClazz(), id);
        if (entity == null) {
            throw new NotFoundEntityException(id);
        } else {
            return entity;
        }
    }

    @Override
    public List<T> getAll()  {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(getClazz());
            Root<T> root = cq.from(getClazz());

            return em.createQuery(cq).getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void delete(T entity)  {
        try {
            em.remove(entity);
        } catch (Exception e) {
            throw new DaoException("Delete entity failed.", e);
        }
    }

    @Override
    public void update(T entity)  {
        try {
            em.merge(entity);
        } catch (Exception e) {
            throw new DaoException("Update entity failed.",e);
        }
    }

}
