package ru.javawebinar.topjava.service;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class UserServiceTest {

    //
    @Autowired
    private UserService service;
    private ExpectedException thrown = ExpectedException.none();


    // Rules
    @ClassRule
    public static TimerRule timerResult = new TimerRule(1);

    @Rule
    public RuleChain ruleChain = RuleChain
            .outerRule(new TimerRule(0))
            .around(thrown);


    // Tests
    @Test
    public void testSave() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, Collections.singleton(Role.ROLE_USER));
        User created = service.save(newUser);
        newUser.setId(created.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, newUser, USER), service.getAll());
    }

    @Test
    public void testDuplicateMailSave() throws Exception {
        thrown.expect(DataAccessException.class);
        service.save(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(USER_ID);
        MATCHER.assertCollectionEquals(Collections.singletonList(ADMIN), service.getAll());
    }

    @Test
    public void testNotFoundDelete() throws Exception {
        thrown.expect(NotFoundException.class);
        service.delete(1);
    }

    @Test
    public void testGet() throws Exception {
        User user = service.get(USER_ID);
        MATCHER.assertEquals(USER, user);
    }

    @Test
    public void testGetNotFound() throws Exception {
        thrown.expect(NotFoundException.class);
        service.get(1);
    }

    @Test
    public void testGetByEmail() throws Exception {
        User user = service.getByEmail("user@yandex.ru");
        MATCHER.assertEquals(USER, user);
    }

    @Test
    public void testGetAll() throws Exception {
        Collection<User> all = service.getAll();
        MATCHER.assertCollectionEquals(Arrays.asList(ADMIN, USER), all);
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        service.update(updated);
        MATCHER.assertEquals(updated, service.get(USER_ID));
    }

    // Tests Validator
    @Test
    public void testValidatorEmailPattern() throws Exception {
        thrown.expect(Exception.class);
        service.save(new User(null, "New", "email", "newPass", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorEmailNotNull() throws Exception {
        thrown.expect(Exception.class);
        service.save(new User(null, "New", "", "newPass", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorEmail() throws Exception {
        service.save(new User(null, "New", "email@email", "newPass", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorPasswordNotEmpty() throws Exception {
        thrown.expect(Exception.class);
        service.save(new User(null, "New", "email@email", "", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorPasswordLength() throws Exception {
        thrown.expect(Exception.class);
        service.save(new User(null, "New", "email@email", "1234", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorPassword() throws Exception {
        service.save(new User(null, "New", "email@email", "12345", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorCaloriesInteger() throws Exception {
        thrown.expect(Exception.class);
        service.save(new User(null, "New", "email@email", "12345", 12345, false, Collections.singleton(Role.ROLE_USER)));
    }

    @Test
    public void testValidatorCalories() throws Exception {
        service.save(new User(null, "New", "email@email", "12345", 123, false, Collections.singleton(Role.ROLE_USER)));
        service.save(new User(null, "New", "email@email.ru", "12345", 1234, false, Collections.singleton(Role.ROLE_USER)));
    }
}