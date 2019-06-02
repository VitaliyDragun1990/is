<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<div id="shoppingCart">
	<c:if test="${empty clientSession.account}">
		<div class="alert alert-warning hidden-print" role="alert"><custom:i18n key="app.cart.msg.signIn"/></div>
	 </c:if>
     <ishop:product-table items="${clientSession.shoppingCart.items}" showActionColumn="true" totalCost="${clientSession.shoppingCart.totalCost}"/>
          
	<div class="row hidden-print">
		<div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
          	<c:choose>
          		<c:when test="${empty clientSession.account}">
             		<ishop:sign-in classes="btn-block"/>
          		</c:when>
          		<c:otherwise>
          			<a class="post-request btn btn-primary btn-block" href="javascript:void(0);" data-url='<c:url value="/order" />'>
          				<custom:i18n key="app.button.makeOrder"/>
          			</a>
          		</c:otherwise>
         	</c:choose>
		</div>
	</div>
</div>