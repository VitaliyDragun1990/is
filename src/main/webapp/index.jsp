<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>Order form</title>
</head>
<body>
	<form action="/order" method="post">
		<input type="hidden" name="id_customer" value="<%=request.getParameter("idCustomer")%>">
		Product name: <input name="product"><br>
		Product count:<input name="count" type="number"><br>
		<input type="submit" value="Make order">
	</form>
	
	<p>Lorem ipsum dolor sit amet, consectetur adipisicing elit.
	 Possimus facere accusantium aspernatur ipsum qui maxime vitae at vero eius dolore reiciendis atque explicabo nisi
	  sint officia eaque blanditiis minima nobis.</p>
	  
	  <h4>Some text</h4>
	  
	 <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Enim officiis molestias ullam cum officia ad dolorum est natus 
	 tempora nam mollitia eos incidunt libero. Cupiditate quis adipisci qui recusandae ab.</p>
	 
	 <ul>
	 	<li>list item</li>
	 	
	 	
	 	
	 	<li>list item</li>
	 	
	 	
	 	
	 	
	 	<li>list item</li>
	 	
	 	
	 	
	 	<li>list item</li>
	 	
	 	
	 	<li>list item</li>
	 	
	 	
	 	
	 	
	 	<li>list item</li>
	 	
	 	
	 	
	 	<li>list item</li>
	 	
	 </ul>
</body>
</html>