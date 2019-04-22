<%@ page import="com.revenat.ishop.model.ShoppingCart" %>
<%@ page import="com.revenat.ishop.model.ShoppingCart.ShoppingCartItem" %>
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>Shopping Cart Page</title>
</head>
<body>
	<h2>Shopping cart content</h2>
	
	<c:set var="cart" value="${CURRENT_SHOPPING_CART}" />
	<c:choose>
		<c:when test="${!(empty cart)}">
			Total count = ${cart.totalCount} <br />
			<c:if test="${cart.totalCount > 0}">
				Products: <br />
				<c:forEach var="item" items="${cart.items}">
					${item.productId}-&gt;${item.quantity} <br />
				</c:forEach>
	
			</c:if>
		</c:when>
		<c:otherwise>
			Shopping cart not found.
		</c:otherwise>
	</c:choose>
	
</body>
</html>