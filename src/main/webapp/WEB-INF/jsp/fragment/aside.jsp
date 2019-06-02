<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<div class="visible-xs-block xs-option-container">
	<a class="pull-right" data-toggle="collapse" href="#productCatalog"><custom:i18n key="app.aside.panel.productCatalog"/> <span class="caret"></span></a>
	<a data-toggle="collapse" href="#findProducts"><custom:i18n key="app.aside.panel.findProducts"/> <span class="caret"></span></a>
</div>
<!-- Search form -->
<form class="search-form" action='<c:url value="/search" />'>
	<div id="findProducts" class="panel panel-success collapse">
		<div class="panel-heading"><custom:i18n key="app.aside.panel.findProducts"/></div>
		<div class="panel-body">
			<div class="input-group">
				<input type="text" name="query" class="form-control" placeholder='<custom:i18n key="app.search.query"/>' value="${searchForm.query}">
				<span class="input-group-btn">
					<a id="goSearch" class="btn btn-default"><custom:i18n key="app.button.search"/></a>
				</span>
			</div>
			<div class="more-options">
				<a data-toggle="collapse" href="#searchOptions"> <custom:i18n key="app.button.moreFilters"/> <span class="caret"></span></a>
			</div>
		</div>
		<!-- Search filters  -->
		<div id="searchOptions" class="collapse ${!empty searchForm.categories || !empty searchForm.producers ? 'in' : ''}" >
			<ishop:category-filter categories="${filterCategories}" checked="${searchForm.categories}"/>
			<ishop:producer-filter producers="${filterProducers}" checked="${searchForm.producers}" />
		</div>
	</div>
</form>
<!-- /Search form -->
<!-- Categories -->
<div id="productCatalog" class="panel panel-success collapse">
	<div class="panel-heading"><custom:i18n key="app.aside.panel.productCatalog"/></div>
	<div class="list-group">
	<c:forEach var="c" items="${allCategories}" >
		<a href='<c:url value="/products${c.url}" />' class="list-group-item ${selectedCategoryUrl == c.url ? 'active' : ''}">
			<span class="badge">${c.productCount}</span><c:out value="${c.name }"/>
		</a>
	</c:forEach>
	</div>
</div>
<!-- /Categories -->