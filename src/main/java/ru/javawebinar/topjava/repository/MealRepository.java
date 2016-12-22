package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

/**
 * GKislin
 * 06.03.2015.
 */
public interface MealRepository {
    Meal save(int userId, Meal Meal);

    boolean delete(int userId, int id);

    Meal get(int userId, int id);

    Collection<Meal> getAll();
}
