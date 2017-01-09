package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkislin
 * Date: 26.08.2014
 */

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMealRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public Meal save(Meal meal, int userId) {
        SqlParameterSource map = new MapSqlParameterSource()
                .addValue("id",             meal.getId())
                .addValue("user_id",        userId)
                .addValue("description",    meal.getDescription())
                .addValue("calories",       meal.getCalories())
                .addValue("date_time",      meal.getDateTime());

        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id = ? and user_id = ?", ROW_MAPPER, meal.getId(), userId);
        Meal actual = DataAccessUtils.singleResult(meals);
        if (actual != null) {
            namedParameterJdbcTemplate.update("UPDATE meals " +
                            "SET " +
                            "user_id        = :user_id, " +
                            "description    = :description, " +
                            "calories       = :calories, " +
                            "date_time      = :date_time " +
                            "WHERE id       = :id",
                    map);
        } else {
            if (meal.isNew()) {
                Number newId = simpleJdbcInsert.executeAndReturnKey(map);
                meal.setId(newId.intValue());
            } else {
                return null;
            }
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id = ? AND user_id = ?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id = ? and user_id = ?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);

//        return jdbcTemplate.queryForObject("SELECT * FROM meals WHERE ID = ?", ROW_MAPPER, id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? ORDER BY date_time DESC", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE user_id = ? and date_time BETWEEN ? and ? ORDER BY date_time DESC", ROW_MAPPER, userId, startDate, endDate);
    }
}
