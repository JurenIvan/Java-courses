<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<style>
		body {background-color : #${pickedBgColor}}
	</style>
</head>

<body>
	<a href="setcolor?color=FFFFFF">White</a><br>
	<a href="setcolor?color=0000FF">Blue</a><br>
	<a href="setcolor?color=FF0000">Red</a><br>
	<a href="setcolor?color=00FF00">Green</a><br>
	<a href="setcolor?color=00FFFF">Cyan</a><br>
</body>

</html>