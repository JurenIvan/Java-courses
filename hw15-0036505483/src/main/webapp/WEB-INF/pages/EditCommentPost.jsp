<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Comment</title>
		
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
	
		<h1>Blog title:</h1><h2>${post.title}</h2>
		<h2>Blog text:</h2>${post.text}

		<h4>created at:</h4>${post.createdAt}
		<h4>last modified by:</h4>${post.lastModifiedAt}

			<br><hr><br>
			<h2>Comments</h2>
			<br>

		
		<c:forEach var="e" items="${comments}">
			<hr><h4>Posted time:</h4>${e.postedOn} 	
			<p><h4>Comment Email:</h4>${e.usersEMail}
			<p><h4>Comment message:</h4>${e.message}
		</c:forEach>
		
	<br><br><br><hr><br><br><br>
	
		<% if(session.getAttribute("current.user.nick")!=null && session.getAttribute("current.user.nick").equals(request.getAttribute("user"))) { %>
			<a href="edit?id=${post.id}"> EDIT THIS BLOG POST</a>
		<% }%>

		<form method="post">
		
		<% if (session.getAttribute("current.user.id") == null) {%>
			<div>
		 	<div>
		 	 <span class="formLabel">EMail</span><input type="text" name="email" value='<c:out value="${record.email}"/>' size="40">
		 	</div>
		 	<c:if test="${record.hasError('email')}">
		 	<div class="greska"><c:out value="${record.getError('email')}"/></div>
		 	</c:if>
			</div>
		<% }%>

		<div>
		 <div>
		  <span class="formLabel">Message</span><input type="text" name="message" value='<c:out value="${record.message}"/>' size="40">
		 </div>
		 <c:if test="${record.hasError('message')}">
		 <div class="greska"><c:out value="${record.getError('message')}"/></div>
		 </c:if>
		</div>


		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="method" value="Submit">
		  <input type="submit" name="method" value="Cancel">
		</div>
		
		</form>

	</body>
</html>
