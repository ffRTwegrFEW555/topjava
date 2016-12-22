package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GKislin
 * 06.03.2015.
 */

@Service
public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Autowired
    public void setRepository(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Meal m = repository.save(userId, meal);
        if (m == null) {
            throw new NotFoundException("Save error.");
        }
        return m;
    }

    @Override
    public void delete(int userId, int id) {
        boolean b = repository.delete(userId, id);
        if (!b) {
            throw new NotFoundException("Delete error.");
        }
    }

    @Override
    public Meal get(int userId, int id) {
        Meal m = repository.get(userId, id);
        if (m == null) {
            throw new NotFoundException("Get error.");
        }
        return m;
    }

    @Override
    public Collection<MealWithExceed> getAll(int userId, LocalDateTime from, LocalDateTime to) {
        if (from == null) {
            from = LocalDateTime.MIN;
        }

        if (to == null) {
            to = LocalDateTime.MAX;
        }

        List<Meal> mealList = (List<Meal>) repository.getAll();
        if (mealList == null || mealList.size() == 0) {
            return Collections.emptyList();
        }

        mealList = mealList.stream()
                .filter(u -> u.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());

        if (mealList.size() == 0) {
            return Collections.emptyList();
        }

        List<MealWithExceed> mealsWithExceedList = MealsUtil.getFilteredWithExceeded(
                mealList, from, to, AuthorizedUser.getCaloriesPerDay());

        return mealsWithExceedList.size() == 0 ? Collections.emptyList() : mealsWithExceedList;
    }
}
