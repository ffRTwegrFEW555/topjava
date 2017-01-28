package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

/**
 * Created by USER on 28.01.2017, 17:16.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceTestDatajpaTest extends UserServiceTest {

    @Test
    public void testGetWithMealLazy() throws Exception {
        User user = super.service.getWithMealLazy(USER_ID);
        MATCHER.assertEquals(USER, user);

        List<Meal> meals = new ArrayList<>();
        meals.addAll(user.getMeals());
        meals.sort(Comparator.comparing(Meal::getId).reversed());
        Assert.assertTrue(MealTestData.MEALS.equals(meals));
    }
}
