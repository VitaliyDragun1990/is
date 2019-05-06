<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags"%>

<div class="row">
	<div class="col-sm-12">
		<h3 class="text-center">Please sign in before creating an order.</h3>
	</div>
</div>
	
<div class="row hidden-print">
   <div class="col-md-4 col-md-offset-4 col-lg-2 col-lg-offset-5">
     <ishop:sign-in classes="btn-block"/>
   </div>
</div>