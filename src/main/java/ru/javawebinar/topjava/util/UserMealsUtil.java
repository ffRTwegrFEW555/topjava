package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(21, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList,
                                                                   LocalTime startTime,
                                                                   LocalTime endTime,
                                                                   int caloriesPerDay) {

        // class UserMealWithExceed: change "boolean" to "AtomicBoolean";
        // "AtomicBoolean" supported by most json-frameworks, including Jackson;

        List<UserMealWithExceed> mealWithExceedList;
        Map<LocalDate, Integer> mealForDaysCalories = new HashMap<>();
        Map<LocalDate, AtomicBoolean> mealForDaysBoolean = new HashMap<>();

        mealWithExceedList = mealList
                .stream()
                .peek(m -> {
                    LocalDate localDate = m.getDateTime().toLocalDate();

                    mealForDaysCalories.merge(localDate, m.getCalories(), (oldV, newV) -> oldV + newV);

                    mealForDaysBoolean.computeIfAbsent(localDate, v -> new AtomicBoolean(m.getCalories() > caloriesPerDay));
                    mealForDaysBoolean.get(localDate).set(mealForDaysCalories.get(localDate) > caloriesPerDay);
                })
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(
                        m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        mealForDaysBoolean.get(m.getDateTime().toLocalDate())))
                .collect(Collectors.toList());

        return mealWithExceedList;
    }
}
