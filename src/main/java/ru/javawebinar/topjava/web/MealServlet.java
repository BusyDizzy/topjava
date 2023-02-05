package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.services.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebServlet
public class MealServlet extends HttpServlet {

    private static final String INSERT_OR_EDIT = "/addMeal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final MealService mealService;

    public MealServlet() {
        super();
        mealService = new MealService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Starting doGet Method");

        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            log.debug("Starting Delete action");
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealService.getMealById(mealId);
            mealService.removeById(mealId);
            log.info("Meal" + meal.toString() + " was removed");
            forward = LIST_MEAL;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealService.getAllMeal(), null, null, mealService.caloriesPerDay);
            request.setAttribute("meals", mealToList);
        } else if (action.equalsIgnoreCase("edit")) {
            log.info("Starting Edit action");
            forward = INSERT_OR_EDIT;
            int mealId = Integer.parseInt(request.getParameter("mealId"));
            Meal meal = mealService.getMealById(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")) {
            log.debug("Starting show all meals action");
            forward = LIST_MEAL;
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealService.getAllMeal(), null, null, mealService.caloriesPerDay);
            log.info("Meal to MealTo executed");
            request.setAttribute("meals", mealToList);
        } else {
            forward = INSERT_OR_EDIT;
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
        log.debug("Existing doGet");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        log.debug("Starting POST method");
        Meal meal = new Meal();
        meal.setDescription(request.getParameter("description"));
        LocalDateTime mealDate = LocalDateTime.parse(request.getParameter("dateTime"), dateTimeFormatter);
        meal.setDateTime(mealDate);
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        String mealId = request.getParameter("mealId");
        if (mealId == null || mealId.isEmpty()) {
            mealService.addMeal(meal);
            log.info("New meal: " + meal + " added");
        } else {
            meal.setId(Integer.parseInt(mealId));
            mealService.update(meal.getId(), meal);
            log.info("Meal" + meal + " was updated");
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealService.getAllMeal(), null, null, MealService.caloriesPerDay);
        request.setAttribute("meals", mealToList);
        view.forward(request, response);
        log.info("Existing doPOST");
    }
}
