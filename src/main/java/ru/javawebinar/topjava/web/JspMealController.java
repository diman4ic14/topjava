package ru.javawebinar.topjava.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends MealRestController {

    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping
    public String save(HttpServletRequest request, Model model) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            create(meal);
        } else {
            update(meal, getId(request));
        }
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping()
    public String getMeals(Model model, HttpServletRequest request) {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete" -> {
                return deleteMeal(model, request);
            }
            case "create", "update" -> {
                return createOrUpdateMeal(model, request);
            }
            case "filter" -> {
                return filter(model, request);
            }
            default -> {
                model.addAttribute("meals", getAll());
                return "meals";
            }
        }

    }

    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    public String createOrUpdateMeal(Model model, HttpServletRequest request) {
        String action = request.getParameter("action");
        final Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now(), "", 1000) :
                get(getId(request));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    public String deleteMeal(Model model, HttpServletRequest request) {
        delete(getId(request));
        model.addAttribute("meals", getAll());
        return "meals";
    }

    public int getId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("id"));
    }

}
