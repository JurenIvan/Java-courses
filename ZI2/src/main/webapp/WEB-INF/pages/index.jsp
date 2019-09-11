<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Welcome!</title>
</head>


<body>
	<form action="imageRedirect" method="get">
		<table >
			<tr>
				<td>Select An Item :</td>
				<td><select name="itemSelected">
						<c:forEach var="item" items="${items}">
							<option value="${item}">${item}</option>
						</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td><input type="submit" value="Send"></td>
				<td></td>
			</tr>
		</table>
	</form>
</body>

</html>