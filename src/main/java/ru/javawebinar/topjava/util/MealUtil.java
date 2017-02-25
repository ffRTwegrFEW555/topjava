package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

/**
 * Created by USER on 25.02.2017, 11:39.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
public class MealUtil {

    public static Meal createNewFromTo(MealTo mealTo) {
        return new Meal(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories());
    }

    public static MealTo asTo(Meal meal) {
        return new MealTo(meal.getId(), meal.getDateTime(), meal.getDescription(), meal.getCalories());
    }

    public static Meal updateFromTo(Meal meal, MealTo mealTo) {
        meal.setDateTime(mealTo.getDateTime());
        meal.setDescription(mealTo.getDescription());
        meal.setCalories(mealTo.getCalories());
        return meal;
    }
}
