<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<h4 class="text-center">My orders</h4>
<hr />
<div id="orderList" data-page-total="${totalPageCount}" data-page-current="1">
	<table class="table table-bordered">
		<thead>
			<tr>
				<th>Order ID</th>
				<th>Created</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty orders }">
				<tr>
					<td colspan="2" class="text-center">No orders found</td>
				</tr>
			</c:if>
			<jsp:include page="../fragment/order-list.jsp"/>
		</tbody>
	</table>
	
	<div class="text-center hidden-print">
		<c:if test="${totalPageCount > 1}">
			<a id="loadMoreOrders" class="btn btn-success">Load more orders</a>
		</c:if>
	</div>
</div>