<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div id="productList" data-page-total="${totalPageCount}"  data-page-current="1">
	<div class="row">
		<jsp:include page="../fragment/product-list.jsp"/>
	</div>
	
	<c:if test="${totalPageCount > 1}">
	<div class="text-center hidden-print">
		<a id="loadMore" class="btn btn-success">Load more products</a>
	</div>
	</c:if>
</div>

<ishop:add-product-popup/>