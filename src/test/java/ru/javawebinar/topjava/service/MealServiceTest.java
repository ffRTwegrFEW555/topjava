package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.MealTestData.*;


/**
 * Created by USER on 05.01.2017, 22:01.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        MEALS = new ArrayList<>(MealsUtil.MEALS);
        dbPopulator.execute();
    }

    @Test
    public void testGet() throws Exception {
        Meal expected   = MEALS.get(0);
        Meal actual     = mealService.get(100002, 100000);
        MATCHER.assertEquals(expected, actual);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        mealService.get(200002, 100000);
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFoundWrongUser() throws Exception {
        mealService.get(100002, 100001);
    }

    @Test
    public void testDelete() throws Exception {
        mealService.delete(100002, 100000);
        MEALS.remove(0);
        MEALS.sort(Comparator.comparing(Meal::getDateTime).reversed());
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(100000));
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFound() throws Exception {
        mealService.delete(200002, 100000);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteNotFoundWrongUser() throws Exception {
        mealService.delete(100002, 100001);
    }

    @Test
    public void testGetBetweenDates() throws Exception {
        LocalDate localDate = LocalDate.of(2015, Month.MAY, 30);
        Collection<Meal> expected = mealService.getBetweenDates(localDate, localDate, 100000);
        List<Meal> actual = MEALS.stream()
                .filter((m) -> m.getDate().compareTo(localDate) >= 0 && m.getDate().compareTo(localDate) <= 0)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        MATCHER.assertCollectionEquals(expected, actual);
    }

    @Test
    public void testGetBetweenDateTimes() throws Exception {
        LocalDateTime start = LocalDateTime.of(2015, Month.MAY, 30, 10, 0, 0);
        LocalDateTime end   = LocalDateTime.of(2015, Month.MAY, 30, 13, 0, 0);
        Collection<Meal> expected = mealService.getBetweenDateTimes(start, end, 100000);
        List<Meal> actual = MEALS.stream()
                .filter((m) -> m.getDateTime().compareTo(start) >= 0 && m.getDateTime().compareTo(end) <= 0)
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        MATCHER.assertCollectionEquals(expected, actual);

    }

    @Test
    public void testGetAll() throws Exception {
        MEALS.sort(Comparator.comparing(Meal::getDateTime).reversed());
        MATCHER.assertCollectionEquals(MEALS, mealService.getAll(100000));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак!!!", 500);
        expected.setId(100002);
        mealService.update(expected, 100000);
        MATCHER.assertEquals(expected, mealService.get(100002, 100000));
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFound() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак!!!", 500);
        expected.setId(200002);
        mealService.update(expected, 100000);
    }

    @Test(expected = NotFoundException.class)
    public void testUpdateNotFoundWrongUser() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак!!!", 500);
        expected.setId(100002);
        mealService.update(expected, 100001);
    }

    @Test
    public void testSave() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак!!!", 500);
        mealService.save(expected, 100000);
        MATCHER.assertEquals(expected, mealService.get(100010, 100000));
        Assert.assertTrue(expected.getId() == 100010);
    }

}