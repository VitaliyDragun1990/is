<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" uri="/WEB-INF/tld/custom.tld" %>

<%@ attribute name="classes" required="false" type="java.lang.String" %>

<form class="sign-in-form" action='<c:url value="/sign-in" />' method="post">
	<button class="btn btn-primary ${classes}">
		<i class="fa fa-facebook-official" aria-hidden="true"></i> <custom:i18n key="app.button.signIn"/>
	</button>
</form>
