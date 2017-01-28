package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;

/**
 * Created by USER on 28.01.2017, 17:15.
 *
 * @author Vadim Gamaliev <a href="mailto:gamaliev-vadim@yandex.com">gamaliev-vadim@yandex.com</a>
 * @version 1.0
 */
@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDatajpaTest extends MealServiceTest {
}