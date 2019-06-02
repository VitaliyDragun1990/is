<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

 <!-- Header section -->
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#ishopNav" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href='<c:url value="/products" />'><custom:i18n key="app.title"/></a>
        </div>
        <div class="collapse navbar-collapse" id="ishopNav">
          <ul id="currentShoppingCart" class="nav navbar-nav navbar-right ${clientSession.shoppingCart.isEmpty() ? 'hidden' : ''}">
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                <i class="fa fa-shopping-cart" aria-hidden="true"></i>
                <custom:i18n key="app.button.cart"/> (<span class="cart-total-count">${clientSession.shoppingCart.totalCount}</span>)
                <span class="caret"></span>
              </a>
              <div class="dropdown-menu shopping-cart-desc">
                <custom:i18n key="app.label.totalCount"/>: <span class="cart-total-count">${clientSession.shoppingCart.totalCount}</span><br>
                <custom:i18n key="app.label.totalCost"/>: <span class="cart-total-cost">${clientSession.shoppingCart.totalCost}</span><br>
                <a href='<c:url value="/shopping-cart" />' class="btn btn-primary btn-block"><custom:i18n key="app.button.viewCart"/></a>
              </div>
            </li>
          </ul>
          <c:choose>
          	<c:when test="${clientSession.account != null}">
          		<ul class="nav navbar-nav navbar-right">
          			<li><img class="avatar" src="${clientSession.account.avatarUrl }" alt="user avatar" /></li>
          			<li><a><custom:i18n key="app.label.welcome" args="${clientSession.account.description}"/></a></li>
          			<li><a href='<c:url value="/my-orders" />'><custom:i18n key="app.button.myOrders"/></a></li>
          			<li>
          				<ishop:sign-out/>
          			</li>
          		</ul>
          	</c:when>
          	<c:when test="${CURRENT_REQUEST_URL != '/sign-in' and CURRENT_REQUEST_URL != '/shopping-cart'}">
          		<ishop:sign-in classes="navbar-btn navbar-right sign-in"/>
          	</c:when>
          </c:choose>

        </div>
      </div>
    </nav>