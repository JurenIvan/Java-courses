<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>New User Registration</title>
		
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
		<h1>New user</h1>

		<form action="register" method="post">
		
		<div>
		 <div>
		  <span class="formLabel">First name:</span><input type="text" name="firstName" value='<c:out value="${record.firstName}"/>' size="5">
		 </div>
		 <c:if test="${record.hasError('firstName')}">
		 <div class="greska"><c:out value="${record.getError('firstName')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Last name:</span><input type="text" name="lastName" value='<c:out value="${record.lastName}"/>' size="5">
		 </div>
		 <c:if test="${record.hasError('lastName')}">
		<div class="greska"><c:out value="${record.getError('lastName')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Email:</span><input type="text" name="email" value='<c:out value="${record.email}"/>' size="5">
		 </div>
		  <c:if test="${record.hasError('email')}">
		<div class="greska"><c:out value="${record.getError('email')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Nick:</span><input type="text" name="nick" value='<c:out value="${record.nick}"/>' size="5">
		 </div>
		 <c:if test="${record.hasError('nick')}">
		<div class="greska"><c:out value="${record.getError('nick')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Password:</span><input type="text" name="password" value='<c:out value="${record.password}"/>' size="5">
		 </div>
		 <c:if test="${record.hasError('password')}">
		 <<div class="greska"><c:out value="${record.getError('pass')}"/></div>
		 </c:if>
		</div>
		

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="metoda" value="Register">
		  <input type="submit" name="metoda" value="Cancel">
		</div>
		
		</form>

	</body>
</html>
