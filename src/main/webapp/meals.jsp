<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<c:set var="meals" value="${requestScope.mealsToList}"/>
<table class="tg">
    <tr>
        <th width="30">ID</th>
        <th width="120">Date/Time</th>
        <th width="200">Description</th>
        <th width="50">Calories</th>
        <th width="30"></th>
        <th width="30"></th>
    </tr>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <tr style="color: ${meal.excess ? "red" : "green"}">
            <td>${meal.id}</td>
            <td>${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy HH:mm')}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td>Edit</td>
            <td>Delete</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>