package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by USER on 01.02.2017, 19:22.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@Controller
@RequestMapping(path = "/meals")
public class MealController {

    private static final Logger LOG = getLogger(MealController.class);

    @Autowired
    MealService service;


    /*
        GET
     */

    @GetMapping
    public String getAll(Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("Get all meals");

        model.addAttribute("meals", MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay()));
        return "meals";
    }

    @GetMapping(params = "action=create")
    public String creating(Model model) {
        LOG.info("Opening the page to create a meal");

        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "meal";
    }

    @GetMapping(params = "action=update")
    public String updating(@RequestParam(value = "id") int id, Model model) {
        int userId = AuthorizedUser.id();
        LOG.info("Opening the page for update meal {} with user {}: TRY", id, userId);


        Meal meal = null;
        try {
            meal = service.get(id, userId);
            model.addAttribute("openPageForUpdateResult", true);
            LOG.info("Opening the page for update meal {} with user {}: SUCCESS", id, userId);
        } catch (Exception e) {
            model.addAttribute("openPageForUpdateResult", false);
            LOG.info("Opening the page for update meal {} with user {}: FAIL", id, userId);
        }
        model.addAttribute("meal", meal);
        return "meal";
    }

    @GetMapping(params = "action=delete")
    public String delete(@RequestParam(value = "id") int id, RedirectAttributes redirectAttributes) {
        int userId = AuthorizedUser.id();
        LOG.info("Deleting meal {} with user id {}: TRY", id, userId);

        try {
            service.delete(id, userId);
            redirectAttributes.addFlashAttribute("deleteResult", true);
            LOG.info("Deleting meal {} with user id {}: SUCCESS", id, userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("deleteResult", false);
            LOG.info("Deleting meal {} with user id {}: FAIL", id, userId);
        }
        return "redirect:meals";
    }


    /*
        POST
     */

    @PostMapping(params = "id=")
    public String create(
            @RequestParam(value = "dateTime")       String dateTime,
            @RequestParam(value = "description")    String description,
            @RequestParam(value = "calories")       int calories,
            RedirectAttributes redirectAttributes) {

        int userId = AuthorizedUser.id();
        LOG.info("Creating meal with user id {}: TRY", userId);

        Meal meal = new Meal(LocalDateTime.parse(dateTime), description, calories);
        try {
            service.save(meal, userId);
            redirectAttributes.addFlashAttribute("createResult", true);
            LOG.info("Creating meal with user id {}: SUCCESS", userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("createResult", false);
            LOG.info("Creating meal with user id {}: FAIL", userId);
        }
        return "redirect:meals";
    }

    @PostMapping(params = "id!=")
    public String update(
            @RequestParam(value = "id")             int id,
            @RequestParam(value = "dateTime")       String dateTime,
            @RequestParam(value = "description")    String description,
            @RequestParam(value = "calories")       int calories,
            RedirectAttributes redirectAttributes) {

        int userId = AuthorizedUser.id();
        LOG.info("Updating meal {} with user id {}: TRY", id, userId);

        Meal meal = new Meal(id, LocalDateTime.parse(dateTime), description, calories);
        try {
            service.update(meal, userId);
            redirectAttributes.addFlashAttribute("updateResult", true);
            LOG.info("Updating meal {} with user id {}: SUCCESS", id, userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("updateResult", false);
            LOG.info("Updating meal {} with user id {}: FAIL", id, userId);
        }
        return "redirect:meals";
    }

    @PostMapping(params = "action=filter")
    public String getAllFiltered(
            @RequestParam(required = false, value = "startDate")  String startDateString,
            @RequestParam(required = false, value = "endDate")    String endDateString,
            @RequestParam(required = false, value = "startTime")  String startTimeString,
            @RequestParam(required = false, value = "endTime")    String endTimeString,
            Model model) {

        int userId = AuthorizedUser.id();
        LOG.info("Get all meals with filter with user {}", userId);

        LocalDate startDate = DateTimeUtil.parseLocalDate(startDateString);
        LocalDate endDate   = DateTimeUtil.parseLocalDate(endDateString);
        LocalTime startTime = DateTimeUtil.parseLocalTime(startTimeString);
        LocalTime endTime   = DateTimeUtil.parseLocalTime(endTimeString);

        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(
                service.getBetweenDates(
                        startDate   != null ? startDate : DateTimeUtil.MIN_DATE,
                        endDate     != null ? endDate   : DateTimeUtil.MAX_DATE,
                        userId),

                startTime   != null ? startTime : LocalTime.MIN,
                endTime     != null ? endTime   : LocalTime.MAX,
                AuthorizedUser.getCaloriesPerDay()
        );

        model.addAttribute("meals", meals);
        return "meals";
    }


}
