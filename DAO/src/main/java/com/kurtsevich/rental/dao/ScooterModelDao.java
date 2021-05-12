package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IScooterModelDao;
import com.kurtsevich.rental.model.ScooterModel;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterModelDao extends AbstractDao<ScooterModel> implements IScooterModelDao {
    @Override
    protected Class<ScooterModel> getClazz() {
        return ScooterModel.class;
    }
}
