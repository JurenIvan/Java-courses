<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Welcome!</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		   padding-left: 110px;
		}
		.formLabel {
		   display: inline-block;
		   width: 100px;
                   font-weight: bold;
		   text-align: right;
                   padding-right: 10px;
		}
		.formControls {
		  margin-top: 10px;
		}
		</style>
		
	</head>

	<body>
	
		<jsp:include page="CurrentUser.jsp" />
		
		<%
		if(session.getAttribute("current.user.id")==null) {
		%>
		
			<form action="login" method="post">
			<div>
			 <div>
			  <span class="formLabel">Nick:</span><input type="text" name="nick" value='<c:out value="${loginEntry.nick}"/>' size="20">
			 </div>
			 <c:if test="${loginEntry.hasError('nick')}">
			 <div class="greska"><c:out value="${loginEntry.getError('nick')}"/></div>
			 </c:if>
			</div>
	
			<div>
			 <div>
			  <span class="formLabel">Password:</span><input type="text" name="pass" />' size="50">
			 </div>
			 <c:if test="${loginEntry.hasError('pass')}">
			 <div class="greska"><c:out value="${loginEntry.getError('password')}"/></div>
			 </c:if>
			</div>
	
			<div class="formControls">
			  <span class="formLabel">&nbsp;</span>
			  <input type="submit" name="method" value="Log in">
			</div>
			</form>
		
			<br><br><br>
			<p><a href="newUser">Register a new user</a></p>
	
		<%}%>
	
		<br><br><br>
		<c:forEach var="e" items="${blogUsers}">
			<li><a href="Odi na autora">${e.nick}</a></li>
		</c:forEach>
	
	
	
	</body>
</html>