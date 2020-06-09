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
<table border="1">
    <thead>
    <th>Date</th>
    <th>Description</th>
    <th>Calories</th>
    </thead>
    <tbody>
    <c:forEach items="${meals}" var="meal">
        <c:if test="${meal.excess}" var="isExcess">
            <tr style="color: red">
                <td>${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy HH:mm')}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:if>
        <c:if test="${!meal.excess}" var="isNotExcess">
            <tr style="color: green">
                <td>${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy HH:mm')}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:if>
    </c:forEach>
    </tbody>
</table>
</body>
</html>