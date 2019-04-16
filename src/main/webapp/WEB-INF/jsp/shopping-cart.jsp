<%@ page import="com.revenat.ishop.model.ShoppingCart" %>
<%@ page import="com.revenat.ishop.model.ShoppingCart.ShoppingCartItem" %>
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<title>Shopping Cart Page</title>
</head>
<body>
	<h2>Shopping cart content</h2>
	Total count = ${!(empty cart) ? cart.totalCount : 0} <br />
	
	<% ShoppingCart cart = (ShoppingCart) request.getAttribute("cart");
	if (cart != null) { %>
	
	Products: <br />
	
	<% for (ShoppingCartItem item : cart.getItems()) {%>
		<%= item.getProductId()%>-><%=item.getQuantity()%> <br />
	<% }} %> <br />
	
</body>
</html>