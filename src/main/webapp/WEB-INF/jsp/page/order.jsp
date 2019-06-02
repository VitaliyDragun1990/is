<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<div id="order">
	<c:if test="${!empty CURRENT_MESSAGE}">
		<div class="alert alert-success hidden-print" role="alert">${CURRENT_MESSAGE}</div>
	</c:if>
	<h4 class="text-center"><custom:i18n key="app.order.header" args="${CURRENT_ORDER.id}"/></h4>
	<hr />
	<ishop:product-table items="${CURRENT_ORDER.items}" showActionColumn="false" totalCost="${CURRENT_ORDER.totalCost}"/>
	<!-- Button to my-orders page -->
	<div class="row hidden-print">
		<div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
			<a class="btn btn-primary btn-block" href='<c:url value="/my-orders" />'>
          		<custom:i18n key="app.button.myOrders"/>
          	</a>
		</div>
	</div>
</div>