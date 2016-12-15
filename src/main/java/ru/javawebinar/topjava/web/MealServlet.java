package ru.javawebinar.topjava.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDaoImplMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by USER on 14.12.2016, 13:42.
 *
 * @author Vadim Gamaliev <gamaliev-vadim@yandex.com>
 * @version 1.0
 */
public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private static final MealDaoImplMemory mealDaoImplMemory = new MealDaoImplMemory();
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equalsIgnoreCase(action)) { // Add
            LOG.debug("Add meal: forward to 'meal.jsp'");
            req.getRequestDispatcher("meal.jsp").forward(req, resp);

        } else if ("update".equalsIgnoreCase(action)) { // Update
            LOG.debug("Update meal: forward to 'meal.jsp'");
            req.setAttribute(
                    "meal",
                    mealDaoImplMemory.getById(Integer.parseInt(req.getParameter("id"))));
            req.getRequestDispatcher("meal.jsp").forward(req, resp);

        } else if ("remove".equalsIgnoreCase(action)) { // Remove
            String id = req.getParameter("id");
            LOG.debug("remove meal with ID: " + id);
            mealDaoImplMemory.remove(Integer.parseInt(id));
            resp.sendRedirect("meals");

        } else { // Page Meals
            LOG.debug("add attribute 'mealsList with Exceed' and forward to 'meals.jsp'");
            req.setAttribute("mealsList", MealsUtil.getFilteredWithExceeded(
                    mealDaoImplMemory.getAll(),
                    LocalTime.MIN,
                    LocalTime.MAX,
                    2000
            ));
            req.getRequestDispatcher("meals.jsp").forward(req, resp);

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //
        req.setCharacterEncoding("UTF-8");

        // Action
        String action = req.getParameter("action");

        // Id
        int id = 0;
        try {
            id = Integer.parseInt(req.getParameter("mealId"));
        } catch (Exception ignored) {
        }

        // LocalDateTime
        LocalDateTime localDateTime = null;
        String dateTimeText         = req.getParameter("mealDateTime");

        if (dateTimeText.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")) {
            try {
                localDateTime = LocalDateTime.parse(
                        dateTimeText,
                        dateTimeFormatter);
            } catch (Exception e) {
                localDateTime = LocalDateTime.now();
            }
        } else {
            localDateTime = LocalDateTime.now();
        }

        // Calories
        int calories = 0;
        try {
            calories = Integer.parseInt(req.getParameter("mealCalories"));
        } catch (Exception ignored) {
        }

        // Description
        String description = req.getParameter("mealDescription");

        // Add and Update
        if ("add".equalsIgnoreCase(action)) {
            LOG.debug("Add meal: redirect to 'meals.jsp'");

            Meal meal = new Meal(
                    MealDaoImplMemory.getNewId(),
                    localDateTime,
                    description,
                    calories);
            mealDaoImplMemory.add(meal);

        } else if ("update".equalsIgnoreCase(action)) {
            LOG.debug("Update meal: redirect to 'meals.jsp'");

            if (id != 0) {
                Meal meal = new Meal(
                        id,
                        localDateTime,
                        description,
                        calories);
                mealDaoImplMemory.update(meal);

            }
        }

        //
        resp.sendRedirect("meals");
    }
}
