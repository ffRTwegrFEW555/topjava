package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

/**
 * GKislin
 * 06.03.2015.
 */
@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    @Transactional
    public Meal getWithUserLazy(int id, int userId) throws NotFoundException {
        Meal meal = get(id, userId);
        meal.getUser().isEnabled();
        return meal;
    }

    @CacheEvict(value = "meals", allEntries = true)
    @Override
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Collection<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        Assert.notNull(startDateTime, "startDateTime must not be null");
        Assert.notNull(endDateTime, "endDateTime  must not be null");
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    @Override
    @Cacheable(value = "meals")
    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    @CacheEvict(value = "meals", allEntries = true)
    @Override
    public Meal update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @CacheEvict(value = "meals", allEntries = true)
    @Override
    public Meal save(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
    }

    @CacheEvict(value = "meals", allEntries = true)
    @Override
    public void evictCache() {
    }
}
