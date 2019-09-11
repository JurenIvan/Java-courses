<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
</head>

<body>

	
	<h2>List files:</h2>
		<c:forEach var="e" items="${files}">
			<li><a href="openFile/${e.fileName}">Nick: ${e.fileName} </a></li>
		</c:forEach>
	
	<form method="post">
		
		<div>
		 <div>
		  <span class="formLabel">fileName</span><input type="text" name="fileName" size="40">
		 </div>
		</div>
		
		<div>
		 <div>
		  <span class="formLabel">Body</span><input type="text" name="body" size="400">
		 </div>
		</div>

		<div class="formControls">
		  <span class="formLabel">&nbsp;</span>
		  <input type="submit" name="method" value="Save blog Entry">
		</div>
		
	</form>

	
</body>

</html>