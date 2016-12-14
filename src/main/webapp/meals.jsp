<%--
  User: Gamaliev Vadim
  Date: 14.12.2016
  Time: 13:58
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals list</title>
    <style>
        table {
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 15px;
            text-align: left;
        }
    </style>
</head>
<body>
<a href="index.html">return to Home</a>
<h1>Meals list</h1>

<c:choose>
    <c:when test="${not empty mealsList}">
        <table>
            <thead>
            <tr>
                <th>description</th>
                <th>date / time</th>
                <th>calories</th>
                <th>edit</th>
                <th>remove</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${mealsList}" var="meals">
                <tr style="color: ${meals.exceed ? 'red' : 'green'}"/>
                    <td>${meals.description}</td>
                    <td>
                        <fmt:parseDate value="${meals.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" type="both" var="mealDateTime"/>
                        <fmt:formatDate value="${mealDateTime}" pattern="yyyy-MM-dd HH:mm"/>
                    </td>
                    <td>${meals.calories}</td>
                    <td><a href="meals?action=update&id=${meals.id}">edit</a></td>
                    <td><a href="meals?action=remove&id=${meals.id}">remove</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <p>Meals list is empty, please add meal</p>
    </c:otherwise>
</c:choose>

<a href="meals?action=add">add meal</a>
</body>
</html>
