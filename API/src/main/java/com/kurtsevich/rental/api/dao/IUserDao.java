package com.kurtsevich.rental.api.dao;

import com.kurtsevich.rental.model.User;

public interface IUserDao extends GenericDao<User>{
    User findByUsername(String username);
}
