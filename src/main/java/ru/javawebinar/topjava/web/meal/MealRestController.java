package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * GKislin
 * 06.03.2015.
 */
@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public void setService(MealService service) {
        this.service = service;
    }

    public Meal save(Meal meal) {
        return service.save(AuthorizedUser.id(), meal);
    }

    public void delete(int id) {
        service.delete(AuthorizedUser.id(), id);
    }

    public Meal get(int id) {
        return service.get(AuthorizedUser.id(), id);
    }

    public Collection<MealWithExceed> getAll(LocalDateTime from, LocalDateTime to) {
        return service.getAll(AuthorizedUser.id(), from, to);
    }

}
