package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IRentTermsDao;
import com.kurtsevich.rental.model.RentTerms;
import org.springframework.stereotype.Repository;

@Repository
public class RentTermsDao extends AbstractDao<RentTerms> implements IRentTermsDao {
    @Override
    protected Class<RentTerms> getClazz() {
        return RentTerms.class;
    }
}
