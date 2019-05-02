<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>

<div id="shoppingCart">
          <div class="alert alert-warning hidden-print" role="alert">To place order, please sign in</div>
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>Product</th>
                <th>Price</th>
                <th>Quantity</th>
                <th class="hidden-print">Action</th>
              </tr>
            </thead>
            <tbody>
            	<jsp:include page="../fragment/shopping-cart-items.jsp"/>
            </tbody>
          </table>
          <!-- Sign in to order button -->
          <div class="row hidden-print">
            <div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
              <a class="btn btn-primary btn-block"><i class="fa fa-facebook-official" aria-hidden="true"></i> Sign in</a>
            </div>
          </div>
        </div>