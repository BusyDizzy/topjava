<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr/>

<h2><c:out value="${not empty meal.id ? 'Edit meal' : 'Add meal'}"/></h2>
<form method="POST" action='meals' name="frmAddMeal">

    <input type="hidden" name="mealId"
           value="${meal.id}"/>

    Description : <input
        type="text" name="description"
        value="${meal.description}"/> <br/>
    DateTime : <input
        type="datetime-local" name="dateTime"
        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="date"/>
        <fmt:formatDate value="${parsedDate}" var="formattedDate" type="both" pattern="yyyy-MM-dd HH:mm"/>
        value="${formattedDate}"/>
    <br/>
    Calories : <input
        type="number" name="calories"
        value="${meal.calories}"/> <br/>

    <input type="submit" value="Save"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>