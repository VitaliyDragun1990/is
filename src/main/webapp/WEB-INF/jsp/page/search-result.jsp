<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<div class="alert alert-info">
	<p><custom:i18n key="app.search.resultMsg" args="${productCount}"/></p>
</div>

<jsp:include page="products.jsp"/>