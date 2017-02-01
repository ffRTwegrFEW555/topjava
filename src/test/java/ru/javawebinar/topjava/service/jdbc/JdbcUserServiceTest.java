package ru.javawebinar.topjava.service.jdbc;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;

    @Override
    @Test
    @Ignore
    @Transactional("dataSourceTransactionManager")
    public void testRoles() throws Exception {
        super.testRoles();
    }

    @Override
    @Test
    @Ignore
    public void testValidation() throws Exception {
        super.testValidation();
    }
}