package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

/**
 * gkislin
 * 02.10.2016
 */
@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    //
    // TODO: Cache
    //

    Meal getByIdAndUserId(int id, int userId);

    @EntityGraph(attributePaths = {"user"})
    Meal getWithUserByIdAndUserId(int id, int userId);


    List<Meal> getByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> getByUserIdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);

    @Override
    @Transactional
    Meal save(Meal meal);

    @Transactional
    int deleteByIdAndUserId(int id, int userId);
}
