package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    List<Meal> listMeals();

    Meal getMealById(long id);

    void saveMeal(Meal meal);

    void updateMeal(Meal meal);

    void deleteMeal(long id);
}
