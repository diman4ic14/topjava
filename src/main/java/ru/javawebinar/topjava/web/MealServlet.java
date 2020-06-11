package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final MealRepository mealRepository = MealRepositoryImpl.getInstance();
    private static final Logger log = getLogger(MealServlet.class);
    private static final String LIST_MEALS = "/meals.jsp";
    private static final String INSERT_OR_EDIT = "/meal.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forward = "";
        String action = req.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            long id = Long.parseLong(req.getParameter("id"));
            mealRepository.deleteMeal(id);
            forward = LIST_MEALS;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealRepository.listMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("mealToList", mealToList);
        } else if (action.equalsIgnoreCase("listMeal")) {
            forward = LIST_MEALS;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealRepository.listMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
            req.setAttribute("mealToList", mealToList);
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            long id = Long.parseLong(req.getParameter("id"));
            req.setAttribute("meal",mealRepository.getMealById(id));
        } else {
            forward = INSERT_OR_EDIT;
        }


        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Meal meal = new Meal();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"), dtf);
        meal.setDateTime(dateTime);
        meal.setDescription(req.getParameter("description"));
        meal.setCalories(Integer.parseInt(req.getParameter("calories")));

        String id = req.getParameter("id");

        if (id == null || id.isEmpty()) {
            mealRepository.saveMeal(meal);
        }
        else {
            meal.setId(Long.parseLong(id));
            mealRepository.updateMeal(meal);
        }

        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealRepository.listMeals(), LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("mealToList", mealToList);
        req.getRequestDispatcher(LIST_MEALS).forward(req, resp);
    }
}
