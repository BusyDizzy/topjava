package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
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

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    public static final int CALORIES_PER_DAY = 2000;
    private static final String INSERT_OR_EDIT = "/addMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private MealsRepository mealsRepository;

    @Override
    public void init() {
        mealsRepository = new MemoryBasedMealsRepository();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Starting doGet Method");

        String forward;
        String action = request.getParameter("action");
        if (action == null) {
            action = "listMeal";
        }

        switch (action) {
            case "delete": {
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                if (mealsRepository.removeById(mealId)) {
                    log.info("Meal with id {} was removed", mealId);
                }
                response.sendRedirect(request.getRequestURI());
                return;
            }
            case "create": {
                forward = INSERT_OR_EDIT;
                String mealId = request.getParameter("mealId");
                Meal meal = mealId == null ? new Meal(LocalDateTime.now(), "", 1000) : mealsRepository.getById(Integer.parseInt(mealId));
                request.setAttribute("meal", meal);
                break;
            }
            default: {
                forward = LIST_MEAL;
                log.info("Meal to MealTo executed");
                request.setAttribute("meals", MealsUtil.filteredByStreams(mealsRepository.getAll(),
                        null, null, CALORIES_PER_DAY));
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
        String mealId = request.getParameter("mealId");
        Meal meal = new Meal(mealId.isEmpty() ? null : Integer.valueOf(mealId),
                LocalDateTime.parse(request.getParameter("dateTime"), DATE_TIME_FORMATTER),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        log.info(meal.isNew() ? "Object {} was created" : "Object {} was updated", meal);
        mealsRepository.add(meal);
        response.sendRedirect(request.getRequestURI());
        log.info("Exit doPOST");
    }
}
