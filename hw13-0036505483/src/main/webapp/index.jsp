<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
	<style>
		body {background-color: #${pickedBgColor}}
	</style>
</head>

<body>
	<a href="colors.jsp">Background color chooser</a>
	
	<br><br><br>
	<a href="trigonometric?a=0&b=90">Angles 0-90</a>
	
	<br>

	<form action="trigonometric" method="GET">
		Početni kut:<br>
		<input type="number" name="a" min="0" max="360" step="1" value="0"><br>
		Završni kut:<br>
		<input type="number" name="b" min="0" max="360" step="1" value="360"><br>
		<input type="submit" value="Tabeliraj"><input type="reset"
			value="Reset">
	</form>
	<br><br><br>
	
	<a href="stories\funny.jsp">Funny story</a>
	<br><br><br>
	
	<a href="report.jsp">Report</a>
	<br><br><br>
	
	<a href="powers?a=1&b=100&n=3">Powers</a>
	<br><br><br>
	
	<a href="appinfo.jsp">App info</a>
	<br><br><br>
	
	<a href="glasanje">Glasanje</a>
	<br><br><br>
	
	
</body>

</html>