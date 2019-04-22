<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<title>Products Page</title>
</head>
<body>

	<c:forEach var="p" items="${products}">
		<div class="col-xs-12 xol-sm-6 col-md-4 col-lg-3 col-xlg-2">
			<div id="product${p.id}" class="panel panel-default product">
				<h2>${fn:toUpperCase(p.name)}</h2>
				<div class="price">
					Price: <fmt:formatNumber value="${p.price}" type="currency" currencyCode="USD" />
				</div>
				<div class="created">
					Created: <fmt:formatDate value="${p.created}" pattern="yyyy-MM-dd"/>
				</div>
				<div class="created">
					Created: <fmt:formatDate value="${p.created}" dateStyle="long" type="date"/>
				</div>
			</div>
		</div>
	</c:forEach>

</body>
</html>