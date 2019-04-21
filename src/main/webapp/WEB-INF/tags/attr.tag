<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<%@ attribute name="condition" required="true" type="java.lang.Boolean" rtexprvalue="true" %>
<% if(condition) {%>
	Condition is true
<% } else { %>
	Condition is false
<% } %>