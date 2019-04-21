<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="core" uri="/WEB-INF/tld/core.tld" %>
<!DOCTYPE html>
<html>
<head>
<title>Custom ForEach tag test page</title>
</head>
<body>
	<h2>Custom ForEach tag Test Page</h2>
	<br /><br />
	
	<h4>Null Object test</h4>
	<core:forEach items="${nullObj}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Iterable test</h4>
	<core:forEach items="${iterable}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Iterator test</h4>
	<core:forEach items="${iterator}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Array test</h4>
	<core:forEach items="${array}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>String tokens test</h4>
	<core:forEach items="${tokens}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Empty string test</h4>
	<core:forEach items="${emptyString}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Map test</h4>
	<core:forEach items="${map}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<h4>Map test 2</h4>
	<core:forEach items="${map}" var="item">
		${item.getKey()} - ${item.getValue() } <br />
	</core:forEach>
	<hr />
	
	<h4>Enumeration test</h4>
	<core:forEach items="${enumeration}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	
	<%--
	<h4>Wrong type test</h4>
	<core:forEach items="${wrongType}" var="item">
		${item} <br />
	</core:forEach>
	<hr />
	 --%>

</body>
</html>