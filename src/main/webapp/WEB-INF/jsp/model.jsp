<%@ page import="com.revenat.ishop.model.Model" %>
<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<title>JSP Demo Page</title>
</head>
<body>
	model=${model} <br />
	model.list=${model.list} <br />
	foreach1=
	<% Model model = (Model) request.getAttribute("model");
	for (String item : model.getList()) {%>
		<%= item %> ||
	<%} %> <br />
	
	foreach2=
	<% model = (Model) request.getAttribute("model");
	for(String item : model.getList()) {
		pageContext.setAttribute("item", item);
	%>
		${item } ||
	<%} %> <br>
	
	foreach3= ${model.data} <br />
</body>
</html>