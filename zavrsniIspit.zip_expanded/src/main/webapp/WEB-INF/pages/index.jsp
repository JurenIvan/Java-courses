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
	<ul>
	<c:forEach items="${files}" var="entry">
		<li><a href="picture?name=${entry}">${entry}</a></li>
	</c:forEach>
	</ul>

	<form action="upload" method="post">
		<div>
			<span>File name:</span>
			<input type="text" name="ime"></input>
		</div>
		<div>
			<span>File text:</span>
			<textarea name="tekst" rows="20" cols="50"></textarea>
		</div>
		<input type="submit" value="Submit">
	</form>

</body>
</html>
