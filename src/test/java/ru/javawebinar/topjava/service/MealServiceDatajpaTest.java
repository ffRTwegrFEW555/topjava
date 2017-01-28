package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL1;
import static ru.javawebinar.topjava.MealTestData.ADMIN_MEAL_ID;
import static ru.javawebinar.topjava.MealTestData.MATCHER;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

/**
 * Created by USER on 28.01.2017, 17:15.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDatajpaTest extends MealServiceTest {

    @Test
    public void testGetWithUserLazy() throws Exception {
        Meal actual = super.service.getWithUserLazy(ADMIN_MEAL_ID, ADMIN_ID);
        MATCHER.assertEquals(ADMIN_MEAL1, actual);
        Assert.assertTrue(ADMIN_ID == actual.getUser().getId());
    }
}