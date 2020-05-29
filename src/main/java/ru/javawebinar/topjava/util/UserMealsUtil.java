package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMealWithExcess> listWithExcess = new ArrayList<>();

        Map<LocalDate, List<UserMeal>> mealsPerDay = new HashMap<>();

        for (UserMeal userMeal : meals) {
            if (!mealsPerDay.containsKey(userMeal.getDateTime().toLocalDate())) {
                List<UserMeal> list = new ArrayList<>();
                list.add(userMeal);
                mealsPerDay.put(userMeal.getDateTime().toLocalDate(), list);
            } else {
                mealsPerDay.computeIfPresent(userMeal.getDateTime().toLocalDate(), (k, v) -> {
                    v.add(userMeal);
                    return v;
                });
            }
        }


        for (Map.Entry<LocalDate, List<UserMeal>> entry : mealsPerDay.entrySet()) {
            List<UserMeal> mealList = entry.getValue();
            int countCalories = mealList.stream().mapToInt(UserMeal::getCalories).sum();

            for (UserMeal userMeal : mealList) {
                if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                    listWithExcess.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                            userMeal.getCalories(), countCalories > caloriesPerDay));
                }
            }
        }

        return listWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> mealsByDay = meals
                .stream()
                .collect(Collectors.toMap(userMeal -> userMeal.getDateTime().toLocalDate(),
                        UserMeal::getCalories, (c1, c2) -> c1 += c2));

        return meals
                .stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(),
                        userMeal.getCalories(), mealsByDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
