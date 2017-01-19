package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Meal get(int id, int userId) {
        Meal meal = null;
        try {
            meal = em
                    .createNamedQuery(Meal.GET_WITH_USER_ID, Meal.class)
                    .setParameter("userId", userId)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception ignored) {
        }

        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em
                .createNamedQuery(Meal.GET_ALL_WITH_USER_ID, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em
                .createNamedQuery(Meal.GET_ALL_BETWEEN_DATES_WITH_USER_ID, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            User user = em.getReference(User.class, userId);
            meal.setUser(user);
            em.persist(meal);
            return meal;

        } else {
            return em
                    .createNamedQuery(Meal.UPDATE_WITH_USER_ID)
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("description", meal.getDescription())
                    .setParameter("calories", meal.getCalories())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() == 0 ? null : meal;
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em
                .createNamedQuery(Meal.DELETE_WITH_USER_ID)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }
}