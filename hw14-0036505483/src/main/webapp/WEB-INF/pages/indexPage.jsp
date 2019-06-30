<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.model.*"%>
<%@page import="java.util.List"%>


<%
	List<PollModel> unosi = (List<PollModel>) request.getAttribute("polls");
%>

<html>
<body>
	<b>Pronađeni su sljedeći unosi:</b>
	<br>

	<%if (unosi.isEmpty()) {%>Nema unosa.<%}
	else {%>
	<table style="width:100%">
 	 <tr>
		<c:forEach var="e" items="${polls}" >
		 <tr><th><a href="glasanje?pollID=${e.id }">${e.title }</a></th><th>${e.message }</th></tr>
		 </c:forEach>
	 </tr>
	</table>
	<%}%>
</body>
</html>