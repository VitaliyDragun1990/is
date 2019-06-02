<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<%@ attribute name="producers" required="true" type="java.util.List" rtexprvalue="true" %>
<%@ attribute name="checked" required="true" type="java.util.List" rtexprvalue="true" %>

	<div class="panel-heading"><custom:i18n key="app.aside.panel.producerFilters"/></div>
	<div class="panel-body producers">
		<label><input type="checkbox" id="allProducers"><custom:i18n key="app.aside.label.all"/></label>
		<c:forEach var="p" items="${producers}">
		<div class="form-group">
			<div class="checkbox">
				<label>
				<c:choose>
					<c:when test="${!empty checked && checked.contains(p.id)}">
					<input type="checkbox" name="producer" value="${p.id}" checked class="search-option">
					<c:out value="${p.name}"/> (${p.productCount})
					</c:when>
					<c:otherwise>
					<input type="checkbox" name="producer" value="${p.id}" class="search-option">
					<span class="${!empty checked ? 'text-muted' : ''}">
						<c:out value="${p.name}"/> (${!empty checked && p.productCount > 0 ? '+' : ''} ${p.productCount})
					</span>
					</c:otherwise>
				</c:choose>
				</label>
			</div>
		</div>
		</c:forEach>
	</div>
