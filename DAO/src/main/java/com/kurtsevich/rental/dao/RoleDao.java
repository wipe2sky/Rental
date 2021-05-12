package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IRoleDao;
import com.kurtsevich.rental.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDao extends AbstractDao<Role> implements IRoleDao {
    @Override
    protected Class<Role> getClazz() {
        return Role.class;
    }
}
