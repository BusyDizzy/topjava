<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meal list</title>
    <style>
        .green {
            color: green;
        }

        .red    {
            color: red;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr/>
<h2>Meals</h2>
<p><a href="javascript:void(0)" onclick="location.href='meals?action=create'">Add Meal</a></p>
<table border=1>
    <thead>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealTo"/>
        <tr class="${meal.isExcess() ? 'red' : 'green'}">
            <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="date"/>
            <fmt:formatDate value="${parsedDate}" var="formattedDate" type="both" pattern="yyyy-MM-dd HH:mm" />
            <td>${formattedDate}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="javascript:void(0)" onclick="location.href='meals?action=create&mealId=${meal.id}'">Update</a></td>
            <td><a href="javascript:void(0)" onclick="location.href='meals?action=delete&mealId=${meal.id}'">Delete</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>