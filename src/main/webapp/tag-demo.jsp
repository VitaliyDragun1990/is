<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/tags.tld" %>
<%@ taglib prefix="tag-files" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<title>Page with simple custom tag</title>
</head>
<body>
	<custom:simple-static/> <br />
	
	<custom:attr condition="true"/><br />
	<custom:attr condition="false"/><br />
	<custom:attr condition="${2 > 1}"/><br />
	<custom:attr condition="<%=2 < 1 %>"/><br />
	
	<custom:h>h6</custom:h>
	<custom:h type="h5">h5</custom:h>
	<custom:h type="h4">h4</custom:h>
	<custom:h type="h3">h3</custom:h>
	<custom:h type="h2">h2</custom:h>
	<custom:h type="h1">h1</custom:h>
	<custom:h type="h0">h0</custom:h>
	
	<custom:loop count="-2">NULL</custom:loop><br />
	<custom:loop count="5">5</custom:loop><br />
	<custom:loop count="${1 + 2}">${1 + 2}</custom:loop><br />
	<custom:loop count="<%=1+4 %>">
		<custom:attr condition="${2 > 1 }"/>
	</custom:loop><br />
	
	<custom:uppercase>This string must be uppercased!</custom:uppercase><br />
	
	<hr />
	
	<tag-files:simple/> <br />
	<tag-files:attr condition="true"/> <br />
	<tag-files:attr condition="false"/> <br />
	<tag-files:attr condition="${2 > 1}"/> <br />
	<tag-files:attr condition="<%=2 < 1%>"/> <br />
	
	<hr />
	
	<tag-files:modal message="Test" id="test1" title="TITLE"/> <br /><br />
	
	<tag-files:modal>
		<jsp:attribute name="id">test</jsp:attribute>
		<jsp:attribute name="message"><span><strong>Hello world</strong></span></jsp:attribute>
	</tag-files:modal>
</body>
</html>