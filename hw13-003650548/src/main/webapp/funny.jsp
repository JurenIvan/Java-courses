<%@page import="java.util.Random"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<style>
		body {background-color : #${pickedBgColor}}
	</style>
</head>
<% String color="#"+Integer.toHexString((new Random()).nextInt()%16777215); %>
<body>
	<h1><font color=<%=color%>>Raskida jedan internet domenu sa drugim i kaže mu "Nije do tebe domene je" @Cvija, 2k19</font></h1>
</body>

</html>