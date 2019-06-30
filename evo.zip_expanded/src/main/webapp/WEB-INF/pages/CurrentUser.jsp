<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
.container {
  position: fixed;
  width:80%;
  height:40px;
  left:10%;
  top:10px;
}
</style>

<html>
	<body>
		<% if (session.getAttribute("current.user.id") != null) {%>
			<div class="container">
  				<h3>Current user:ivan  <a href="logout">(Odjava)</a></h3>
			</div>
		<% }%>
		
	
	</body>
</html>
	