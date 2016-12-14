package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by USER on 14.12.2016, 15:22.
 *
 * @author Vadim Gamaliev <gamaliev-vadim@yandex.com>
 * @version 1.0
 */
public class MealDaoImplMemory implements MealDao {
    private static AtomicInteger idCount    = new AtomicInteger();
    private static List<Meal> mealsList     = MealsUtil.getMealsList();

    static {
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        mealsList.add(new Meal(MealDaoImplMemory.getNewId(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    @Override
    public void add(Meal meal) {
        mealsList.add(meal);
    }

    @Override
    public void update(Meal meal) {
        remove(meal.getId());
        mealsList.add(meal);
    }

    @Override
    public void remove(int id) {
        mealsList.removeIf(m -> m.getId() == id);
    }

    @Override
    public List<Meal> getAll() {
        return mealsList;
    }


    @Override
    public Meal getById(int id) {
        for (Meal m : mealsList) {
            if (m.getId() == id) {
                return m;
            }
        }

        return null;
    }

    public static int getNewId() {
        return MealDaoImplMemory.idCount.incrementAndGet();
    }
}
