<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="/assets/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <div class="alert alert-danger">
        <h4 class="alert-heading">
            <c:choose>
                <c:when test="${not empty errorTitle}">${errorTitle}</c:when>
                <c:otherwise>Error <c:out value="${statusCode}"/></c:otherwise>
            </c:choose>
        </h4>
        <p>
            <c:choose>
                <c:when test="${not empty errorMessage}">${errorMessage}</c:when>
                <c:otherwise>An error occurred while processing your request.</c:otherwise>
            </c:choose>
        </p>
        <c:if test="${not empty requestUri}">
            <hr>
            <p class="mb-0"><small>URL: <c:out value="${requestUri}"/></small></p>
        </c:if>
    </div>
    <a href="/" class="btn btn-primary">Go to Home</a>
</div>
</body>
</html>
