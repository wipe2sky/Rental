package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IUserProfileDao;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileDao extends AbstractDao<UserProfile> implements IUserProfileDao {
    @Override
    protected Class<UserProfile> getClazz() {
        return UserProfile.class;
    }
}
