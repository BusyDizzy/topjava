package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private MealRestController mealRestController;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init() {
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                null);
        if (id.isEmpty()) {
            log.info("Create {}", meal);
            mealRestController.create(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            log.info("Update {}", meal);
            mealRestController.update(meal, meal.getId());
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, null) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");

                if (request.getParameterNames().hasMoreElements()) {
                    LocalDate startDate = request.getParameter("startDate").isEmpty() ?
                            LocalDate.MIN : LocalDate.parse(request.getParameter("startDate"));
                    LocalDate endDate = request.getParameter("endDate").isEmpty() ?
                            LocalDate.MAX : LocalDate.parse(request.getParameter("endDate"));
                    LocalTime startTime = request.getParameter("startTime").isEmpty() ?
                            LocalTime.MIN : LocalTime.parse(request.getParameter("startTime"));
                    LocalTime endTime = request.getParameter("endTime").isEmpty() ?
                            LocalTime.MAX : LocalTime.parse(request.getParameter("endTime"));
                    // Я не могу передавать данные без проверки, потому что если значение null,
                    // во время парсинга даты/времени возникает Exception
                    request.setAttribute("meals",
                            mealRestController.getAllFiltered(startDate, endDate, startTime, endTime));
                } else {
                    request.setAttribute("meals",
                            mealRestController.getAll());
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
