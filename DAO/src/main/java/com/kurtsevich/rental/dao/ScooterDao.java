package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IScooterDao;
import com.kurtsevich.rental.model.Scooter;
import org.springframework.stereotype.Repository;

@Repository
public class ScooterDao extends AbstractDao<Scooter> implements IScooterDao {
    @Override
    protected Class<Scooter> getClazz() {
        return Scooter.class;
    }
}
