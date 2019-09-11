<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
<title>Stranica</title>

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
	Image name: ${name}
	<br>
	Number of lines : ${line}
	<br>
	Number of circles : ${circles}
	<br>
	Number of filled circles : ${filled}
	<br>
	Number of triangles : ${triangles}

	<br>	
	<img src="slika?name=${name}">
	<br>

	<a href="main">Back to home page</a>


</body>
</html>
