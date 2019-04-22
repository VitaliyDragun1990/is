<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<title>Profile Page</title>
</head>
<body>

	<div class="row profiles">
		<div id="profileContainer" class="col-xs-12">
			<c:if test="${fn:length(profiles) == 0}">
				Profiles not found
			</c:if>
			<c:forEach var="profile" items="${profiles}">
				<div class="panel panel-default profile-item">
					<img src="${profile.smallPhoto}" alt="profile picture" class="photo" />
					<h3>${profile.fullName}</h3>
				</div>
			</c:forEach>
		</div>
		<c:if test="${page.number < page.totalPages - 1}">
			<div id="loadMoreContainer" class="col-xs-12 text-center">
				<a href="#" class="btn btn-primary">Load more</a>
			</div>
		</c:if>
	</div>

</body>
</html>