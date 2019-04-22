<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="condition" required="true" type="java.lang.Boolean" rtexprvalue="true" %>

<c:choose>
	<c:when test="${condition}">Condition is true</c:when>
	<c:otherwise>Condition is false</c:otherwise>
</c:choose>