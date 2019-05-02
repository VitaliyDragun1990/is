<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ishop" tagdir="/WEB-INF/tags" %>

<div class="visible-xs-block xs-option-container">
	<a class="pull-right" data-toggle="collapse" href="#productCatalog">Product catalog <span class="caret"></span></a>
	<a data-toggle="collapse" href="#findProducts">Find products <span class="caret"></span></a>
</div>
<!-- Search form -->
<form class="search-form" action='<c:url value="/search" />'>
	<div id="findProducts" class="panel panel-success collapse">
		<div class="panel-heading">Find products</div>
		<div class="panel-body">
			<div class="input-group">
				<input type="text" name="query" class="form-control" placeholder="Search query" value="${searchForm.query}">
				<span class="input-group-btn">
					<a id="goSearch" class="btn btn-default">Go!</a>
				</span>
			</div>
			<div class="more-options">
				<a data-toggle="collapse" href="#searchOptions"> More filters <span class="caret"></span></a>
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
	<div class="panel-heading">Product catalog</div>
	<div class="list-group">
	<c:forEach var="c" items="${allCategories}" >
		<a href='<c:url value="/products${c.url}" />' class="list-group-item ${selectedCategoryUrl == c.url ? 'active' : ''}">
			<span class="badge">${c.productCount}</span><c:out value="${c.name }"/>
		</a>
	</c:forEach>
	</div>
</div>
<!-- /Categories -->