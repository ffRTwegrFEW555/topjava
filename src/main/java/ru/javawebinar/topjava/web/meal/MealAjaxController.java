package ru.javawebinar.topjava.web.meal;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by USER on 21.02.2017, 19:45.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@RestController
@RequestMapping(MealAjaxController.AJAX_URL)
public class MealAjaxController extends AbstractMealController {
    static final String AJAX_URL = "/ajax/profile/meals";

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll() {
        return super.getAll();
    }

    @Override
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getBetween(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalTime startTime,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) LocalTime endTime) {

        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createOrUpdate(@RequestParam(required = false) Integer id,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
                                               @RequestParam(required = false) String description,
                                               @RequestParam(required = false) Integer calories) {
        // TODO: may be arg = Meal meal?
        Meal meal = new Meal(
                id,
                dateTime == null        ? LocalDateTime.now() : dateTime,
                description.isEmpty()   ? "null" : description,
                calories == null        ? AuthorizedUser.getCaloriesPerDay() : calories);

        if (meal.isNew()) {
            super.create(meal);
            URI uriOfNewResource = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path(AJAX_URL + "/{id}")
                    .buildAndExpand(meal.getId())
                    .toUri();
            return ResponseEntity.created(uriOfNewResource).body(meal);

        } else {
            super.update(meal, id);
            return ResponseEntity.ok().body(meal);
        }
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}
