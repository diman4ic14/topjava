package ru.javawebinar.topjava.repository;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.slf4j.LoggerFactory.getLogger;

public class MealRepositoryImpl implements MealRepository{
    private final Logger log = getLogger(MealRepositoryImpl.class);
    private final List<Meal> meals = new ArrayList<>();
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

    {
        meals.add( new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(id.getAndIncrement(), LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public List<Meal> listMeals() {
        return meals;
    }

    @Override
    public Meal getMealById(long id) {
        Optional<Meal> optionalMeal = meals.stream()
                .filter(m -> m.getId() == id)
                .findAny();

        Meal meal = null;
        if (optionalMeal.isPresent()) {
            meal = optionalMeal.get();
            log.info("Meal successfully loaded. Meal details: " + meal);
        }
        else
            log.info("Meal isn't found.");

        return meal;
    }

    @Override
    public void saveMeal(Meal meal) {
        meal.setId(id.getAndIncrement());
        meals.add(meal);
        log.info("Meal successfully saved. Meal details: " + meal);
    }

    @Override
    public void updateMeal(Meal meal) {
        Meal oldMeal = getMealById(meal.getId());
        int index = meals.indexOf(oldMeal);
        meals.remove(oldMeal);
        meal.setId(oldMeal.getId());
        meals.add(index, meal);
        log.info("Meal successfully updated. Meal details: " + meal);
    }

    @Override
    public void deleteMeal(long id) {
        Meal meal = getMealById(id);
        if (meal != null) {
            meals.remove(meal);
            log.info("Meal successfully deleted. Meal details: " + meal);
        }
    }
}
