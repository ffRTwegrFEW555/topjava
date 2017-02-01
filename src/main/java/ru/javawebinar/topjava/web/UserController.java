package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by USER on 01.02.2017, 18:26.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@Controller
@RequestMapping(path = "/users")
public class UserController {

    private static final Logger LOG = getLogger(UserController.class);

    @Autowired
    private UserService service;

    @GetMapping
    public String users(Model model) {
        LOG.debug("Get all users");

        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping
    public String setUser(HttpServletRequest request) {
        LOG.debug("Authorize user");

        int userId = Integer.valueOf(request.getParameter("userId"));
        AuthorizedUser.setId(userId);
        return "redirect:meals";
    }
}
