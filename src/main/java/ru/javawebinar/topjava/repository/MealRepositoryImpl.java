package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepositoryImpl implements MealRepository{
    private final Logger log = getLogger(MealRepositoryImpl.class);
    private final Map<Long, Meal> meals = new HashMap<>();
    private final AtomicLong id = new AtomicLong(1);
    private static MealRepositoryImpl mealRepository;

    private MealRepositoryImpl() {
    }

    public static MealRepositoryImpl getInstance() {
        if (mealRepository == null) {
            mealRepository = new MealRepositoryImpl();
        }
        return mealRepository;
    }


    @Override
    public List<Meal> listMeals() {
        log.info("Getting listMeals");
        return new ArrayList<>(meals.values());
    }

    @Override
    public Meal getMealById(long id) {
        Meal meal = meals.get(id);

        if (meal != null)
            log.info("Meal successfully loaded. Meal details: " + meal);
        else
            log.info("Meal isn't found.");

        return meal;
    }

    @Override
    public void saveMeal(Meal meal) {
        meal.setId(id.getAndIncrement());
        meals.put(meal.getId(), meal);
        log.info("Meal successfully saved. Meal details: " + meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        meals.put(meal.getId(), meal);
        log.info("Meal successfully updated. Meal details: " + meal);
    }

    @Override
    public void deleteMeal(long id) {
        Meal meal = meals.get(id);
        if (meal != null) {
            meals.remove(id);
            log.info("Meal successfully deleted. Meal details: " + meal);
        }
    }
}
