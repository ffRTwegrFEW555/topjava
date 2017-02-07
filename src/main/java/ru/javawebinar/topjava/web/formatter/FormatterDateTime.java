package ru.javawebinar.topjava.web.formatter;

import java.lang.annotation.*;

/**
 * Created by USER on 08.02.2017, 0:52.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormatterDateTime {
}
