<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Error!</title>
		
	</head>

	<body>
		<h1>Error occured!</h1>
		<p><c:out value="${error}"/></p>

		<p><a href="../../../index.html">Back to HomePage</a></p>
	</body>
</html>