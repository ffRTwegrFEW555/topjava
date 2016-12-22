package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * GKislin
 * 15.09.2015.
 */
@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> {
            m.setUserId(AuthorizedUser.id());
            save(AuthorizedUser.id(), m);
        });
    }

    @Override
    public Meal save(int userId, Meal meal) {
        try {
            Meal m = repository.get(meal.getId());
            if (userId != m.getUserId()) {
                return null;
            }
            LOG.info("Save: meal with id: " + meal.getId() + " updated.");
        } catch (Exception ignored) {
            LOG.info("Save: meal with id: " + meal.getId() + " not found.");
            meal.setId(counter.incrementAndGet());
            LOG.info("Save: meal with id: " + meal.getId() + " add as new.");
        }

        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        Meal m = get(userId, id);
        return m != null && repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        try {
            Meal meal = repository.get(id);
            if (userId != meal.getUserId()) {
                LOG.info("Get: meal with id: " + id + " invalid userId.");
                return null;
            }
            return meal;
        } catch (Exception e) {
            LOG.info("Get: meal with id: " + id + " not found.");
            return null;
        }
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }

}

