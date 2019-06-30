<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="hr.fer.zemris.java.model.*"%>
<%@page import="java.util.List"%>


<%
	PollModel descriptor = (PollModel) request.getAttribute("descipton");
	List<PollOptionModel> choices = (List<PollOptionModel>) request.getAttribute("options");
%>

<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<h1><%=descriptor.getTitle()%></h1>
	<p><%=descriptor.getMessage()%></p>
	<ol>
		<c:forEach var="e" items="${options}">
			<li><a href="glasanje-glasaj?id=${e.id}&pollID=<%=descriptor.getId()%>">${e.optionTitle}</a></li>
		</c:forEach>
	</ol>
</body>
</html>




