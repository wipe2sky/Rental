package com.kurtsevich.rental.api.dao;

import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.UserProfile;

import java.util.List;

public interface IHistoryDao extends GenericDao<History> {
    List<History> getUserHistories(UserProfile userProfile);
    List<History> getActualUserHistories(UserProfile userProfile);

}
