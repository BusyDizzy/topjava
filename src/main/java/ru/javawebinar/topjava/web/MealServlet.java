package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealsRepository;
import ru.javawebinar.topjava.repository.MemoryBasedMealsRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final int CALORIES_PER_DAY = 2000;
    private static final String INSERT_OR_EDIT = "/addMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private MealsRepository mealService;

    @Override
    public void init() {
        mealService = new MemoryBasedMealsRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Starting doGet Method");

        String forward;
        String action = request.getParameter("action");
        if (action == null) {
            action = "listMeal";
        }

        switch (action) {
            case "delete": {
                log.debug("Starting Delete action");
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                if (mealService.removeById(mealId)) {
                    log.info("Meal with id {} was removed", mealId);
                }
                response.sendRedirect(request.getRequestURI());
                return;
            }
            case "edit": {
                log.info("Starting Edit action");
                forward = INSERT_OR_EDIT;
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = mealService.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            }
            case "insert": {
                forward = INSERT_OR_EDIT;
                break;
            }
            default: {
                log.debug("Starting show all meals action");
                forward = LIST_MEAL;
                List<MealTo> mealToList = new ArrayList<>(MealsUtil.filteredByStreams(mealService.getAll(),
                        null, null, CALORIES_PER_DAY));
                log.info("Meal to MealTo executed");
                Collections.sort(mealToList);
                request.setAttribute("meals", mealToList);
                break;
            }
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        log.debug("Exit doGet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("Starting POST method");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime"), DATE_TIME_FORMATTER),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            mealService.add(meal);
            log.info("Object: {} was added ", meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            mealService.update(meal);
            log.info("Object {} was updated", meal);
        }
        response.sendRedirect(request.getRequestURI());
        log.info("Exit doPOST");
    }
}
