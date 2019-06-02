<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<%@ attribute name="items" required="true" type="java.util.Collection" rtexprvalue="true"%>
<%@ attribute name="totalCost" required="true" type="java.lang.Number" rtexprvalue="true"%>
<%@ attribute name="showActionColumn" required="true" type="java.lang.Boolean"%>

<table class="table table-bordered">
	<thead>
		<tr>
			<th><custom:i18n key="app.label.product"/></th>
			<th><custom:i18n key="app.label.price"/></th>
			<th><custom:i18n key="app.label.quantity"/></th>
			<c:if test="${showActionColumn}">
			<th class="hidden-print"><custom:i18n key="app.label.action"/></th>
			</c:if>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="item" items="${items}" >
		<c:set var="p" value="${item.product}" scope="page" />
		<!-- Individual prodict entry  -->
			<tr id="product${p.id}">
				<td class="text-center">
					<img class="small" src='<c:url  value="${p.imageLink}" />' alt="${p.name}"><br>
					${p.name}
				</td>
				<td class="price">$ ${p.price}</td>
				<td class="quantity">${item.quantity}</td>
				<c:if test="${showActionColumn}">
				<td class="hidden-print">
				<c:choose>
				<c:when test="${item.quantity > 1}">
					<a class="btn btn-danger remove-product" data-id-product="${p.id}" data-quantity="1">
						<custom:i18n key="app.button.removeOne"/>
					</a>
					<br /><br />
					<a class="btn btn-danger remove-product all" data-id-product="${p.id}" data-quantity="${item.quantity}">
						<custom:i18n key="app.button.removeAll"/>
					</a>
				</c:when>
				<c:otherwise>
					<a class="btn btn-danger remove-product" data-id-product="${p.id}" data-quantity="1">
						<custom:i18n key="app.button.removeOne"/>
					</a>
				</c:otherwise>
				</c:choose>
				</td>
				</c:if>
			</tr>
		</c:forEach>
		<tr>
			<td colspan="2" class="text-right"><strong><custom:i18n key="app.label.total"/>:</strong></td>
			<td colspan="2" class="cart-total-cost">$ ${totalCost}</td>
		</tr>
	</tbody>
</table>
