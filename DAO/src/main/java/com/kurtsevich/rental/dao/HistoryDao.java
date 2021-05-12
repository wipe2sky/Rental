package com.kurtsevich.rental.dao;

import com.kurtsevich.rental.api.dao.IHistoryDao;
import com.kurtsevich.rental.api.exception.NotFoundException;
import com.kurtsevich.rental.model.History;
import com.kurtsevich.rental.model.UserProfile;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class HistoryDao extends AbstractDao<History> implements IHistoryDao {
    @Override
    protected Class<History> getClazz() {
        return History.class;
    }

    @Override
    public List<History> getUserHistories(UserProfile userProfile) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<History> cq = cb.createQuery(History.class);
            Root<History> root = cq.from(History.class);

            cq.select(root)
                    .where(cb.equal(root.get("user_profile"), userProfile.getId()));
            TypedQuery<History> query = em.createQuery(cq);
            return query.getResultList();
        } catch (Exception e) {
            throw new NotFoundException("Histories of username " + userProfile.getUser().getUsername() + "has not found", e);
        }
    }

    @Override
    public List<History> getActualUserHistories(UserProfile userProfile) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<History> cq = cb.createQuery(History.class);
            Root<History> root = cq.from(History.class);

            cq.select(root)
                    .where(cb.and(
                            cb.equal(root.get("user_profile"), userProfile.getId()),
                            cb.equal(root.get("is_actual"), true)
                            )
                    );
            TypedQuery<History> query = em.createQuery(cq);
            return query.getResultList();
        } catch (Exception e) {
            throw new NotFoundException("History of username " + userProfile.getUser().getUsername() + "has not found", e);
        }
    }
}
