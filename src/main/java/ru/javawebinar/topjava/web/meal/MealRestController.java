package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    private MealService service;


    public List<MealTo> getAllTo() {
        log.info("getAllTo by userId={}", SecurityUtil.authUserId());
        return MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get id={} by userId={}", id, SecurityUtil.authUserId());
        return service.get(id, SecurityUtil.authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} by userId={}", meal, SecurityUtil.authUserId());
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete id={} by userId={}", id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} by userId={}", meal, id, SecurityUtil.authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }


    public List<MealTo> getListByDateTime(LocalDate startDate, LocalDate endDate,
                                          LocalTime startTime, LocalTime endTime) {
        if (startDate == null)
            startDate = LocalDate.MIN;

        if (endDate == null)
            endDate = LocalDate.MAX;

        if (startTime == null)
            startTime = LocalTime.MIN;

        if (endTime == null)
            endTime = LocalTime.MAX;

        log.info("get filtered MealTo list  by date between {} and {}, time from {} to {} by userId={}",
                startDate, endDate, startTime, endTime, SecurityUtil.authUserId());
        List<Meal> mealList = service.getListByDate(SecurityUtil.authUserId(), startDate, endDate);
        return MealsUtil.getFilteredTos(mealList, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<Meal> getAll() {
        log.info("getAll by {}", SecurityUtil.authUserId());
        return service.getAll(SecurityUtil.authUserId());
    }
}