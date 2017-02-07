package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static ru.javawebinar.topjava.MealTestData.*;

/**
 * Created by USER on 07.02.2017, 22:11.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
public class MealRestControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = MealRestController.BASE_URL + "/";

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(MEALS_WITH_EXCEED_JSON));
    }

    @Test
    public void testGetById() throws Exception {
        Meal meal = MEAL1;

        mockMvc.perform(get(BASE_URL + meal.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(meal));
    }

    @Test
    public void testGetAllByFilter() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("startDate",    "2015-05-30");
        map.add("endDate",      "2015-05-30");
        map.add("startTime",    "09:00");
        map.add("endTime",      "11:00");

        mockMvc.perform(get(BASE_URL + "filter").params(map))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(MEAL_WITH_EXCEED_FILTER));
    }

    @Test
    public void testCreate() throws Exception {
        Meal newMeal = new Meal(LocalDateTime.of(2015, Month.MAY, 31, 23, 0), "Обед", 1000);
        String newMealJson = JsonUtil.writeValue(newMeal);
        newMeal.setId(100010);

        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(newMealJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(newMeal));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal meal = MEAL1;
        meal.setDescription("Breakfast");
        String updatedMealJson = JsonUtil
                .writeValue(meal)
                .replace("\"id\":100002,", "");

        mockMvc.perform(
                put(BASE_URL + meal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedMealJson))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get(BASE_URL + meal.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(meal));
    }

    @Test(expected = NotFoundException.class)
    public void testDelete() throws Exception {
        Meal meal = MEAL1;

        mockMvc.perform(delete(BASE_URL + meal.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        try {
            mockMvc.perform(get(BASE_URL + meal.getId()));
        } catch (Exception e) {
            if (e.getCause() instanceof NotFoundException) {
                System.err.println(e.getCause().getMessage());
                throw new NotFoundException(e.getMessage());
            }
        }
    }
}
