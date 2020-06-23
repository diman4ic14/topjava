package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int NOT_FOUND = 20;

    public static final List<Meal> MEALS_USER = Arrays.asList(
            new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410),
            new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500)
            );

    public static final List<Meal> MEALS_ADMIN = Arrays.asList(
            new Meal(9, LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500),
            new Meal(8, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Админ ланч", 510)
            );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)), "NewMeal", 1000);
    }

    public static Meal getUpdated(List<Meal> meals) {
        Meal updated = new Meal(meals.get(0));
        updated.setDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 0)));
        updated.setDescription("Updated description");
        updated.setCalories(1000);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
