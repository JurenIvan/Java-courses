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
		<h1>New BlogPost</h1>

		<form method="post">
		
		<div>
		 <div>
		  <span class="formLabel">Title</span><input type="text" name="title" value='<c:out value="${form.title}"/>' size="5">
		 </div>
		 <c:if test="${form.hasError('title')}">
		 <div class="greska"><c:out value="${form.getError('title')}"/></div>
		 </c:if>
		</div>

		<div>
		 <div>
		  <span class="formLabel">Text</span><input type="text" name="text" value='<c:out value="${form.text}"/>' size="5">
		 </div>
		 <c:if test="${record.hasError('text')}">
		<div class="greska"><c:out value="${form.getError('text')}"/></div>
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
