<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<!DOCTYPE html>
<html>
<head>
<title>Insert title here</title>
</head>
<body>
	<%! private int count1 = 1; %>
	<jsp:declaration>private int count2 = 2;</jsp:declaration>
	
	count1: <%= count1 %> <br />
	count2: <jsp:expression>count2</jsp:expression> <br />
	
	<% System.out.println("Hello"); %>
	<jsp:scriptlet>System.out.println("Hello again");</jsp:scriptlet>
	
	<%-- JSP Comment --%>
	<!-- HTML Comment -->
</body>
</html>