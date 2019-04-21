<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="core" uri="/WEB-INF/tld/core.tld" %>
<!DOCTYPE html>
<html>
<head>
<title>Switch tag Test Page</title>
</head>
<body>
	<h2>Swict tag test page</h2>
 
	<h4>Empty switch</h4>
	<core:switch value="message"/>
	<hr />
	
	<h4>Switch with one match case</h4>
	<core:switch value="message">
		<core:case value="mmmmm">wrong gues</core:case>
		<core:case value="message">correct one</core:case>
	</core:switch>
	<hr />
	
	<h4>Switch with two match case</h4>
	<core:switch value="message">
		<core:case value="mmmmm">wrong gues</core:case>
		<core:case value="message">correct one</core:case>
		<core:case value="message">correct one - again</core:case>
	</core:switch>
	<hr />
	
	<h4>Switch without match case, but with default</h4>
	<core:switch value="message">
		<core:case value="mmmmm">wrong gues</core:case>
		<core:case value="dsdsds">wrong gues again</core:case>
		<core:default>default</core:default>
	</core:switch>
	<hr />
	
	<h4>Switch without match case, and without default</h4>
	<core:switch value="message">
		<core:case value="mmmmm">wrong gues</core:case>
		<core:case value="ppppp">wrong guess again</core:case>
	</core:switch>
	<hr />
	
	<h4>Switch with default only</h4>
	<core:switch value="message">
		<core:default>default only</core:default>
	</core:switch>
	<hr />
	
	
	
	<h4>Switch with null value</h4>
	<core:switch value="<%=null%>">
		<core:default>default for switch with null value</core:default>
	</core:switch>
	<hr />
	
	
	<h4>Switch with null value, and case with null value</h4>
	<core:switch value="${nullVal}">
		<core:case value="${nullVal}">case with null value</core:case>
		<core:default>default for switch with null value</core:default>
	</core:switch>
	<hr />
	
	<%-- 
	<h4>Default without switch</h4>
	<core:default>Default without enclosing switch</core:default>
	<hr />
	--%>
	
	<%-- 
	<h4>Default without switch</h4>
	<core:case value="mmmm">Case without enclosing switch</core:case>
	<hr />
	--%>
</body>
</html>