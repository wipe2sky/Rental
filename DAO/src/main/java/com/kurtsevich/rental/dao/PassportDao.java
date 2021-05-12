package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IPassportDao;
import com.kurtsevich.rental.model.Passport;
import org.springframework.stereotype.Repository;

@Repository
public class PassportDao extends AbstractDao<Passport> implements IPassportDao {
    @Override
    protected Class<Passport> getClazz() {
        return Passport.class;
    }
}
