<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach var="item" items="${CURRENT_SHOPPING_CART.items}" >
	<c:set var="p" value="${item.product}" scope="page" />
	<!-- Individual prodict entry  -->
	<tr id="product${p.id}">
		<td class="text-center">
			<img class="small" src='<c:url  value="${p.imageLink}" />' alt="${p.name}"><br>
			${p.name}
		</td>
		<td class="price">$ ${item.cost}</td>
		<td class="quantity">${item.quantity}</td>
		<td class="hidden-print">
		<c:choose>
		<c:when test="${item.quantity > 1}">
		<a class="btn btn-danger remove-product" data-id-product="${p.id}" data-quantity="1"> Remove one</a>
		<br /><br />
		<a class="btn btn-danger remove-product all" data-id-product="${p.id}" data-quantity="${item.quantity}">
			Remove all
		</a>
		</c:when>
		<c:otherwise>
		<a class="btn btn-danger remove-product" data-id-product="${p.id}" data-quantity="1"> Remove one</a>
		</c:otherwise>
		</c:choose>
		</td>
	</tr>
</c:forEach>
	<!-- Shopping cart total  -->
	<tr>
		<td colspan="2" class="text-right"><strong>Total:</strong></td>
		<td colspan="2" class="cart-total-cost">$ ${CURRENT_SHOPPING_CART.totalCost}</td>
	</tr>