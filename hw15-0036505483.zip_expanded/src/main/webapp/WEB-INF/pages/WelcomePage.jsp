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
		<br><h1>Welcome to blog!</h1><br>
		<jsp:include page="CurrentUser.jsp" />
		
		<%
		if(session.getAttribute("current.user.id")==null) {
		%>
		<hr>
			<h2>LOGIN TO BLOG</h2>
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
			  <span class="formLabel">Password:</span><input type="text" name="pass" />
			 </div>
			 <c:if test="${loginEntry.hasError('pass')}">
			 <div class="greska"><c:out value="${loginEntry.getError('pass')}"/></div>
			 </c:if>
			</div>
	
			<div class="formControls">
			  <span class="formLabel">&nbsp;</span>
			  <input type="submit" name="method" value="Log in">
			</div>
			</form>
		
			<br><br><br>
			<h3>Have no account yet?</h3>
			<a href="newUser">Register now!</a>
	
		<%}%>
	
		<br><hr><br>
		<h2>List of creators:</h2>
		<c:forEach var="e" items="${blogUsers}">
			<li><a href="author/${e.nick}">Nick: ${e.nick} </a></li>
		</c:forEach>
	
	
	
	</body>
</html>