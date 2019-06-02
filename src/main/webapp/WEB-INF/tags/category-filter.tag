<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<%@ attribute name="categories" required="true" type="java.util.List" rtexprvalue="true" %>
<%@ attribute name="checked" required="true" type="java.util.List" rtexprvalue="true" %>

	<div class="panel-heading"><custom:i18n key="app.aside.panel.categoryFilters"/></div>
	<div class="panel-body categories">
		<label><input type="checkbox" id="allCategories"><custom:i18n key="app.aside.label.all"/></label>
		<c:forEach var="c" items="${categories}" >
		<div class="form-group">
			<div class="checkbox">
				<label>
					<c:choose>
						<c:when test="${!empty checked && checked.contains(c.id)}">
							<input type="checkbox" name="category" value="${c.id}" checked class="search-option">
							<c:out value="${c.name}"/> (${c.productCount})
						</c:when>
						<c:otherwise>
							<input type="checkbox" name="category" value="${c.id}" class="search-option">
							<span class="${!empty checked ? 'text-muted' : ''}">
								<c:out value="${c.name}"/> (${!empty checked && c.productCount > 0 ? '+' : ''} ${c.productCount})
							</span>
						</c:otherwise>
					</c:choose>
				</label>
			</div>
		</div>
		</c:forEach>
	</div>
