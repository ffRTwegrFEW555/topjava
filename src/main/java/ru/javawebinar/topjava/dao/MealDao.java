package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;

import java.util.List;

/**
 * Created by USER on 14.12.2016, 15:19.
 *
 * @author Vadim Gamaliev <gamaliev-vadim@yandex.com>
 * @version 1.0
 */
public interface MealDao {
    void add(Meal meal);
    void update(Meal meal);
    void remove(int id);
    List<Meal> getAll();
    List<MealWithExceed> getAllWithExceed();
    Meal getById(int id);


}
