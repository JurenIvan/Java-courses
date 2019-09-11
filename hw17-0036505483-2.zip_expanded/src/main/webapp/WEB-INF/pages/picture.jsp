<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	session="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>


<html>
<body>
	
	<img src="getImage?imageName=${imageName}">

	<br><h5>NUM of lines:${lines}</h5>
	<br><h5>NUM of circles:${circles}</h5>
	<br><h5>NUM of fcircles:${fcircles}</h5>
	<br><h5>NUM of ftriangles:${ftriangles}</h5>
	
<a href="main">Back to home page</a>


</body>
</html>