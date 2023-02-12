package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealTo> getAll(int userId) {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(int userId, String startD, String endD, String startT, String endT) {

        LocalDate startDate = startD.isEmpty() ?
                LocalDate.MIN : LocalDate.parse(startD, DATE_FORMATTER);

        LocalDate endDate = endD.isEmpty() ?
                LocalDate.MAX : LocalDate.parse(endD, DATE_FORMATTER);

        LocalTime startTime = startT.isEmpty() ?
                LocalTime.MIN : LocalTime.parse(startT, TIME_FORMATTER);

        LocalTime endTime = endT.isEmpty() ?
                LocalTime.MAX : LocalTime.parse(endT, TIME_FORMATTER);

        log.info("getAllFiltered");
        return MealsUtil.getFilteredTos(service.getAll(userId), MealsUtil.DEFAULT_CALORIES_PER_DAY, startTime, endTime);
    }

    public Meal get(int id, int userId) {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal, int userId) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void delete(int id, int userId) {
        log.info("delete {}", id);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id, int userId) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, id, userId);
    }

}