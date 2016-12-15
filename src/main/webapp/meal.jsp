<%--
  User: Gamaliev Vadim
  Date: 14.12.2016
  Time: 14:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meal form</title>
    <style>
        table {
            border-collapse: collapse;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<a href="meals">return to Meals</a>
<h1>${param.action == 'add' ? 'Add meal:' : "Update meal:"}</h1>

<form method="POST" action="meals">
    <table>
        <tbody>
        <tr>
            <td>Id:</td>
            <td><input type="text" readonly="readonly" name="mealId" value="<c:out value="${meal.id}"/>"/></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><input type="text" name="mealDescription" value="<c:out value="${meal.description}"/>"/></td>
        </tr>
        <tr>
            <td>Date / Time:</td>
            <td>
                <c:choose>
                    <c:when test="${not empty meal.dateTime}">
                        <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" type="both" var="mealDateTime"/>
                        <input type="text" name="mealDateTime" value="<fmt:formatDate value="${mealDateTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                    </c:when>
                    <c:otherwise>
                        <input type="text" name="mealDateTime"/>
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
        <tr>
            <td>Calories:</td>
            <td><input type="text" name="mealCalories" value="<c:out value="${meal.calories}"/>"/></td>
        </tr>
        </tbody>
    </table>
    <input type="hidden" name="action" value="${param.action == 'add' ? 'add' : 'update'}"/>
    <input type="submit" value="${param.action == 'add' ? 'add meal' : 'update meal'}"/>
</form>

</body>
</html>
