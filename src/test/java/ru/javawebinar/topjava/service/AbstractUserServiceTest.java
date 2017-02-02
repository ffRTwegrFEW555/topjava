package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.UserTestData.*;

public abstract class AbstractUserServiceTest extends AbstractServiceTest {

    @Autowired
    private Environment environment;

    @Autowired
    protected UserService service;

    @Autowired(required = false)
    protected JpaUtil jpaUtil;

    @Before
    public void setUp() throws Exception {
        try {
            Assume.assumeFalse(environment.acceptsProfiles("jdbc"));
            jpaUtil.clear2ndLevelHibernateCache();
        } catch (Exception ignored) {
        }
        service.evictCache();
    }

    @Test
    public void testSave() throws Exception {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, Collections.singleton(Role.ROLE_USER));
        User created = service.save(newUser);
        newUser.setId(created.getId());

        List<User> usersExpect = Arrays.asList(ADMIN, newUser, USER);
        List<User> usersActual = service.getAll();

        MATCHER.assertCollectionEquals(usersExpect, usersActual);
        MATCHER.assertUserRolesEquals(usersExpect, usersActual);
    }

    @Test(expected = DataAccessException.class)
    public void testDuplicateMailSave() throws Exception {
        service.save(new User(null, "Duplicate", "user@yandex.ru", "newPass", Role.ROLE_USER));
    }

    @Test
    public void testDelete() throws Exception {
        service.delete(USER_ID);

        List<User> usersExpect = Collections.singletonList(ADMIN);
        List<User> usersActual = service.getAll();

        MATCHER.assertCollectionEquals(usersExpect, usersActual);
        MATCHER.assertUserRolesEquals(usersExpect, usersActual);
    }

    @Test(expected = NotFoundException.class)
    public void testNotFoundDelete() throws Exception {
        service.delete(1);
    }

    @Test
    public void testGet() throws Exception {
        User user = service.get(USER_ID);

        MATCHER.assertEquals(USER, user);
        Assert.assertEquals(USER.getRoles(), user.getRoles());
    }

    @Test(expected = NotFoundException.class)
    public void testGetNotFound() throws Exception {
        service.get(1);
    }

    @Test
    public void testGetByEmail() throws Exception {
        User user = service.getByEmail("user@yandex.ru");

        MATCHER.assertEquals(USER, user);
        Assert.assertEquals(USER.getRoles(), user.getRoles());
    }

    @Test
    public void testGetAll() throws Exception {
        List<User> usersExpect = Arrays.asList(ADMIN, USER);
        List<User> usersActual = service.getAll();

        MATCHER.assertCollectionEquals(usersExpect, usersActual);
        MATCHER.assertUserRolesEquals(usersExpect, usersActual);
    }

    @Test
    public void testUpdate() throws Exception {
        User updated = new User(USER);
        updated.setName("UpdatedName");
        updated.setCaloriesPerDay(330);
        service.update(updated);
        User user = service.get(USER_ID);

        MATCHER.assertEquals(updated, user);
        Assert.assertEquals(USER.getRoles(), user.getRoles());
    }

    @Test
    public void testValidation() throws Exception {
        validateRootCause(() -> service.save(new User(null, "  ", "invalid@yandex.ru", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "  ", "password", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "invalid@yandex.ru", "  ", Role.ROLE_USER)), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "invalid@yandex.ru", "password", 9, true, Collections.emptySet())), ConstraintViolationException.class);
        validateRootCause(() -> service.save(new User(null, "User", "invalid@yandex.ru", "password", 10001, true, Collections.emptySet())), ConstraintViolationException.class);
    }
}