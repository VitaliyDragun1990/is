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
	Total count = ${!(empty CURRENT_SHOPPING_CART) ? CURRENT_SHOPPING_CART.totalCount : 0} <br />
	
	<% ShoppingCart cart = (ShoppingCart) session.getAttribute("CURRENT_SHOPPING_CART");
	if (cart != null) { %>
	
	Products: <br />
	
	<% for (ShoppingCartItem item : cart.getItems()) {%>
		<%= item.getProductId()%>-&gt;<%=item.getQuantity()%> <br />
	<% }} %> <br />
	
</body>
</html>