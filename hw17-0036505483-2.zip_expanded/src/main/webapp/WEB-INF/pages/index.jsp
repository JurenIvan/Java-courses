<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>


<html>
<body>
	<b>Pronađeni su sljedeći unosi:</b>
	<br>

	<c:forEach var="e" items="${files}">
		<tr>
			<li><a href="picture?name=${e}">${e}</a></li>
		</tr>
	</c:forEach>



	<form action="upload" method="post">
		<div>
			<span class="formLabel">title</span> 
			<input type="text" name="title" size="40"></input>
		</div>
		<div>
			<span class="formLabel">content</span>
			<textarea name="content" rows="20" cols="50"></textarea>
		</div>

		<input type="submit" value="Submit">
	</form>



</body>
</html>