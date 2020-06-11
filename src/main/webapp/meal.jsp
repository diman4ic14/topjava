<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="ru">
<head>
    <title>Add or edit meal</title>
</head>
<body>

<c:set var="meal" value="${requestScope.meal}"/>
<form method="POST" action='meals' name="frmAddMeal">
    ID : <input type="text" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />" /> <br />
    Date/Time : <input
        type="text" name="dateTime"
        <c:if test="${empty meal}">
            value="dd.MM.yyyy HH:mm"
        </c:if>
        <c:if test="${not empty meal}">
            value="${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy HH:mm')}"
        </c:if> /> <br />
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />" /> <br />
    Calories : <input
        type="text" name="calories"
        value="<c:out value="${meal.calories}" />" /> <br />
    <input
        type="submit" value="Submit" />
</form>
</body>
</html>