package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    @Qualifier("jdbcMealRepository")
    private MealRepository repository;

    @Test
    public void get() {
        Meal meal = service.get(1, USER_ID);
        assertMatch(meal, MEALS_USER.get(MEALS_USER.size() - 1));
    }

    @Test
    public void delete() {
        service.delete(1, USER_ID);
        assertNull(repository.get(1, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealList = service.getBetweenInclusive(LocalDate.of(2020, 1, 31),
                LocalDate.of(2020, 1, 31), USER_ID);
        assertMatch(mealList, Arrays.asList(MEALS_USER.get(0), MEALS_USER.get(1), MEALS_USER.get(2), MEALS_USER.get(3)));
    }

    @Test
    public void getAll() {
        List<Meal> mealList = service.getAll(USER_ID);
        assertMatch(mealList, MEALS_USER);
    }

    @Test
    public void update() {
        Meal updated = getUpdated(MEALS_USER);
        service.update(updated, USER_ID);
        assertMatch(service.get(7, USER_ID), updated);
    }

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteAlien() {
        assertThrows(NotFoundException.class, () -> service.delete(8, USER_ID));
    }

    @Test
    public void getAlien() {
        assertThrows(NotFoundException.class, () -> service.get(9, USER_ID));
    }

    @Test
    public void updateAlien() {
        Meal updated = getUpdated(MEALS_ADMIN);
        assertThrows(NotFoundException.class, () -> service.update(updated, USER_ID));
    }
}