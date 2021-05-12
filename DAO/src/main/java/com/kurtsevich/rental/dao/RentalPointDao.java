package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IRentalPointDao;
import com.kurtsevich.rental.model.RentalPoint;
import org.springframework.stereotype.Repository;

@Repository
public class RentalPointDao extends AbstractDao<RentalPoint> implements IRentalPointDao {
    @Override
    protected Class<RentalPoint> getClazz() {
        return RentalPoint.class;
    }
}
