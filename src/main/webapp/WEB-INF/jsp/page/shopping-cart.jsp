<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>

<div id="shoppingCart">
	<c:if test="${empty CURRENT_USER}">
		<div class="alert alert-warning hidden-print" role="alert">To place order, please sign in</div>
	 </c:if>
     <ishop:product-table items="${CURRENT_SHOPPING_CART.items}" showActionColumn="true" totalCost="${CURRENT_SHOPPING_CART.totalCost}"/>
          
	<div class="row hidden-print">
		<div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
          	<c:choose>
          		<c:when test="${empty CURRENT_USER}">
             		<ishop:sign-in classes="btn-block"/>
          		</c:when>
          		<c:otherwise>
          			<a class="post-request btn btn-primary btn-block" href="javascript:void(0);" data-url='<c:url value="/order" />'>
          				Make order
          			</a>
          		</c:otherwise>
         	</c:choose>
		</div>
	</div>
</div>