<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Welcome!</title>
	</head>

	<body>
		<br><br>
		<jsp:include page="CurrentUser.jsp" />
		
		
		<br><br><br>
		<h2>Content of ${nick}:</h2>
		<c:forEach var="e" items="${posts}">
			<li><a href="${nick}/${e.id}"> ${e.title} </a></li>
		</c:forEach>
		
		<% if(session.getAttribute("current.user.nick")!=null && session.getAttribute("current.user.nick").equals(request.getAttribute("nick"))) { %>
			<a href="<%=session.getAttribute("current.user.nick")%>/new"> New blog entry</a>
		<% }%>
		
	
	</body>
</html>