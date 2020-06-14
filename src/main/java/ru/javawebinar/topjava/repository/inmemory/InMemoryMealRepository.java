package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(SecurityUtil.authUserId());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return isCheckUserId(meal) ?
                repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal) :
                null;
    }

    @Override
    public boolean delete(int id) {
        if (repository.get(id) != null && isCheckUserId(repository.get(id)))
            return repository.remove(id) != null;
        else return false;
    }

    @Override
    public Meal get(int id) {
        Meal meal = repository.get(id);
        return isCheckUserId(meal) ? meal : null;
    }

    public boolean isCheckUserId(Meal meal) {
        return meal.getUserId() == SecurityUtil.authUserId();
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values()
                .stream()
                .filter(meal -> isCheckUserId(meal))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

