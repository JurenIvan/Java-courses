<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<style>
.container {
  position: relative;
  width:60%;
  height:30px;
  left:30%;
  top:5px;
}
</style>

<html>
	<body>
		<% if (session.getAttribute("current.user.id") != null) {%>
			<div class="container">
  				<h3>Current user:name: <%=session.getAttribute("current.user.fn")%> last name:<%=session.getAttribute("current.user.ln")%>  <a href="/blog/servleti/logout">(Log out!)</a></h3>
			</div>
		<% }else{%>
		<div class="container">
  				<h3>Current user: [not logged in] </h3>
			</div>
		<% }%>
	
	</body>
</html>
	