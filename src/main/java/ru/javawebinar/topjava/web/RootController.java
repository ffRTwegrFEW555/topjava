package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: gkislin
 * Date: 22.08.2014
 */
@Controller
@RequestMapping(value = "/")
public class RootController {

    @GetMapping
    public String root() {
        return "index";
    }
}
