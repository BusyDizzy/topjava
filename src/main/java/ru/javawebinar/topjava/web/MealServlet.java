package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MemoryBasedMealsRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet {

    private static final String INSERT_OR_EDIT = "/addMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private MemoryBasedMealsRepositoryImpl memoryBasedMealsRepositoryImpl;

    public MealServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        memoryBasedMealsRepositoryImpl = new MemoryBasedMealsRepositoryImpl();
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
            case "delete":
                log.debug("Starting Delete action");
                int mealId = Integer.parseInt(request.getParameter("mealId"));
                if (memoryBasedMealsRepositoryImpl.removeById(mealId)) {
                    log.info("Meal with id {} was removed", mealId);
                }
                Set<MealTo> mealToList = new ConcurrentSkipListSet<>(MealsUtil.filteredByStreams(memoryBasedMealsRepositoryImpl.getAll(),
                        null, null, User.CALORIES_PER_DAY));
                request.setAttribute("meals", mealToList);
                response.sendRedirect("/topjava/meals");
                return;
            case "edit":
                log.info("Starting Edit action");
                forward = INSERT_OR_EDIT;
                mealId = Integer.parseInt(request.getParameter("mealId"));
                Meal meal = memoryBasedMealsRepositoryImpl.getById(mealId);
                request.setAttribute("meal", meal);
                break;
            case "insert":
                forward = INSERT_OR_EDIT;
                break;
            default:
                log.debug("Starting show all meals action");
                forward = LIST_MEAL;
                mealToList = new ConcurrentSkipListSet<>(MealsUtil.filteredByStreams(memoryBasedMealsRepositoryImpl.getAll(),
                        null, null, User.CALORIES_PER_DAY));
                log.info("Meal to MealTo executed");
                request.setAttribute("meals", mealToList);
                break;
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
            memoryBasedMealsRepositoryImpl.addMeal(meal);
            log.info("Object: {} was added ", meal);
        } else {
            meal.setId(Integer.parseInt(mealId));
            memoryBasedMealsRepositoryImpl.update(meal);
            log.info("Object {} was updated", meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        Set<MealTo> mealToList = new ConcurrentSkipListSet(MealsUtil.filteredByStreams(memoryBasedMealsRepositoryImpl.getAll(),
                null, null, User.CALORIES_PER_DAY));
        request.setAttribute("meals", mealToList);
        view.forward(request, response);
        log.info("Exit doPOST");
    }
}
