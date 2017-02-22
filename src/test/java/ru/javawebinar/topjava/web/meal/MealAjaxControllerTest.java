package ru.javawebinar.topjava.web.meal;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.BaseEntity.START_SEQ;

/**
 * Created by USER on 22.02.2017, 10:45.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
public class MealAjaxControllerTest extends AbstractControllerTest {

    private static final String AJAX_URL = MealAjaxController.AJAX_URL + "/";

    @Autowired
    private MealService service;

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(AJAX_URL).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    public void testGetAllWrongHeader() throws Exception {
        mockMvc.perform(get(AJAX_URL).accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotAcceptable())
                .andDo(print());
    }

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(AJAX_URL + "100002").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testGetWrongHeader() throws Exception {
        mockMvc.perform(get(AJAX_URL + "100002").accept(MediaType.TEXT_HTML))
                .andExpect(status().isNotAcceptable())
                .andDo(print());
    }

    @Test
    public void testCreate() throws Exception {
        Meal created = getCreated();

        ResultActions action = mockMvc.perform(
                post(AJAX_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("dateTime", created.getDateTime().toString())
                        .param("description", created.getDescription())
                        .param("calories", String.valueOf(created.getCalories())))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(header().string("Location", CoreMatchers.endsWith("/ajax/profile/meals/100010")));

        Meal returned = MATCHER.fromJsonAction(action);
        created.setId(returned.getId());

        MATCHER.assertEquals(created, returned);
    }

    @Test
    public void testCreateWrongHeader() throws Exception {
        Meal created = getCreated();

        mockMvc.perform(
                post(AJAX_URL)
                        .accept(MediaType.TEXT_HTML)
                        .param("dateTime",      created.getDateTime().toString())
                        .param("description",   created.getDescription())
                        .param("calories",      String.valueOf(created.getCalories())))
                .andExpect(status().isNotAcceptable())
                .andDo(print());
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = getUpdated();

        ResultActions action = mockMvc.perform(
                post(AJAX_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("id",            String.valueOf(updated.getId()))
                        .param("dateTime",      updated.getDateTime().toString())
                        .param("description",   updated.getDescription())
                        .param("calories",      String.valueOf(updated.getCalories())))
                .andExpect(status().isOk())
                .andDo(print());

        Meal returned = MATCHER.fromJsonAction(action);
        MATCHER.assertEquals(updated, returned);
    }

    @Test
    public void testUpdateWrongHeader() throws Exception {
        Meal updated = getUpdated();

        mockMvc.perform(
                post(AJAX_URL)
                        .accept(MediaType.TEXT_HTML)
                        .param("id",            String.valueOf(updated.getId()))
                        .param("dateTime",      updated.getDateTime().toString())
                        .param("description",   updated.getDescription())
                        .param("calories",      String.valueOf(updated.getCalories())))
                .andExpect(status().isNotAcceptable())
                .andDo(print());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(
                delete(AJAX_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print());

        MATCHER.assertCollectionEquals(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2), service.getAll(START_SEQ));
    }

    @Test(expected = Exception.class)
    public void testDeleteWrongId() throws Exception {
        mockMvc.perform(
                delete(AJAX_URL + "777000"))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }

}