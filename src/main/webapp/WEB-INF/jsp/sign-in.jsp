<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>Login Page</title>
</head>
<body>
	<h2>Please fill in form below to sign-in:</h2> <br />
	<form action="/sign-in" method="post">
		<label for="login">Login: </label>
		<input type="text" name="login" /> <br />
		<label for="password">Password: </label>
		<input type="password" name="password"/> <br />
		<input type="submit" value="SignIn" />
	</form>
</body>
</html>