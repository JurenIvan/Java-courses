<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>BlogEntry</title>
		
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
		
		<h1>
		<c:choose>
		<c:when test="${record.title.isEmpty()}">
		New blog entry
		</c:when>
		<c:otherwise>
		Edit blog entry
		</c:otherwise>
		</c:choose>
		</h1>

		<form method="post">
		
		<div>
		 <div>
		  <span class="formLabel">Title</span><input type="text" name="title" value='<c:out value="${record.title}"/>' size="40">
		 </div>
		 <c:if test="${record.hasError('title')}">
		 <div class="greska"><c:out value="${record.getError('title')}"/></div>
		 </c:if>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Text</span><input type="text" name="text" value='<c:out value="${record.text}"/>' size="200">
		 </div>
		 <c:if test="${record.hasError('text')}">
		 <div class="greska"><c:out value="${record.getError('text')}"/></div>
		 </c:if>
		</div>


		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="method" value="Save blog Entry">
		  <input type="submit" name="method" value="Cancel">
		</div>
		
		</form>

	</body>
</html>
