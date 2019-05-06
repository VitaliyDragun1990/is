<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:forEach var="order" items="${orders}">
	<tr>
		<td><a href='<c:url value="/order?id=${order.id}" />'>Order # ${order.id}</a></td>
		<td class="text-muted">
			<fmt:parseDate value="${ order.created }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
			<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${ parsedDateTime }" />
		</td>
	</tr>
</c:forEach>