package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional
    public User save(User user) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("registered", user.getRegistered())
                .addValue("enabled", user.isEnabled())
                .addValue("caloriesPerDay", user.getCaloriesPerDay());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(map);
            user.setId(newKey.intValue());

            if (user.getRoles() != null && user.getRoles().size() > 0) {
                List<Role> roles = new ArrayList<>(user.getRoles());
                String insertMultipleQUery = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";

                jdbcTemplate.batchUpdate(insertMultipleQUery, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt   (1, user.getId());
                        ps.setString(2, roles.get(i).toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
            }
        } else {
            namedParameterJdbcTemplate.update("UPDATE users SET name=:name, email=:email, password=:password, " +
                    "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map);
            // TODO: create roles update function
        }

        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", id);
        User user = getUser(sqlRowSet);
        return user.isNew() ? null : user;
    }

    @Override
    public User getByEmail(String email) {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", email);
        User user = getUser(sqlRowSet);
        return user.isNew() ? null : user;
    }

    @Override
    public List<User> getAll() {
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet("SELECT * FROM users INNER JOIN user_roles ON users.id = user_roles.user_id");

        Map<Integer, User> users    = new HashMap<>();
        User user                   = null;
        Set<Role> roles             = null;

        while (sqlRowSet.next()) {
            if (!users.containsKey(sqlRowSet.getInt(1))) {
                user = new User();
                roles = new HashSet<>();
                user.setRoles(roles);
                createNewUser(sqlRowSet, user);
                users.put(sqlRowSet.getInt(1), user);
            }
            roles.add(Role.valueOf(sqlRowSet.getString(9)));
        }

        List<User> usersResult = new ArrayList<>(users.values());
        usersResult.sort(Comparator.comparing(User::getEmail));

        return usersResult;
    }


    /*
        Helpers
     */

    private void createNewUser(SqlRowSet sqlRowSet, User user) {
        user.setId(             sqlRowSet.getInt(1));
        user.setName(           sqlRowSet.getString(2));
        user.setEmail(          sqlRowSet.getString(3));
        user.setPassword(       sqlRowSet.getString(4));
        user.setRegistered(     sqlRowSet.getTimestamp(5));
        user.setEnabled(        sqlRowSet.getBoolean(6));
        user.setCaloriesPerDay( sqlRowSet.getInt(7));
    }

    private User getUser(SqlRowSet sqlRowSet) {
        User user       = new User();
        Set<Role> roles = new HashSet<>();
        while (sqlRowSet.next()) {
            if (user.isNew()) {
                createNewUser(sqlRowSet, user);
            }
            roles.add(Role.valueOf(sqlRowSet.getString(9)));
        }
        user.setRoles(roles);
        return user;
    }
}
