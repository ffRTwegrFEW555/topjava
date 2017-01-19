package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * GKislin
 * 11.01.2015.
 */
@Entity
@Table( name = "meals",
        catalog = "topjava",
        uniqueConstraints = @UniqueConstraint(
                name = "meals_unique_user_datetime_idx",
                columnNames = {"user_id", "date_time"}))
@NamedQueries({
        @NamedQuery(name = Meal.GET_WITH_USER_ID,                   query = "SELECT m FROM Meal m WHERE m.id = :id AND m.user.id = :userId"),
        @NamedQuery(name = Meal.GET_ALL_WITH_USER_ID,               query = "SELECT m FROM Meal m WHERE m.user.id = :userId ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.GET_ALL_BETWEEN_DATES_WITH_USER_ID, query = "SELECT m FROM Meal m " +
                                                                            "WHERE m.user.id = :userId " +
                                                                                "AND m.dateTime BETWEEN :startDate AND :endDate " +
                                                                            "ORDER BY m.dateTime DESC"),
        @NamedQuery(name = Meal.UPDATE_WITH_USER_ID,                query = "UPDATE Meal " +
                                                                            "SET dateTime = :dateTime, " +
                                                                                "description = :description, " +
                                                                                "calories = :calories " +
                                                                            "WHERE id = :id AND user.id = :userId"),
        @NamedQuery(name = Meal.DELETE_WITH_USER_ID,                query = "DELETE FROM Meal WHERE id = :id AND user.id = :userId"),

})
public class Meal extends BaseEntity {

    public static final String GET_WITH_USER_ID                     = "Meal.GetWithUserId";
    public static final String GET_ALL_WITH_USER_ID                 = "Meal.GetAllWithUserId";
    public static final String GET_ALL_BETWEEN_DATES_WITH_USER_ID   = "Meal.GetAllBetweenDatesWithUserId";
    public static final String UPDATE_WITH_USER_ID                  = "Meal.UpdateWithUserId";
    public static final String DELETE_WITH_USER_ID                  = "Meal.DeleteWithUserId";

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "calories", nullable = false)
    @Range(max = 9999L, message = "== calories is '${validatedValue}' (must be '0 - {max}') ==")
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
