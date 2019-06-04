<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  

<!DOCTYPE html>
<html>

<head>
	<style>
		body {background-color : #${pickedBgColor}}
	</style>
	<style>
		table {
 		 border-collapse: collapse;
		}

		th, td {
 		 text-align: right;
 		 padding: 8px;
		}

		tr:nth-child(even) {background-color: #f2f2f2;}
	</style>
</head>

<body>
	<table>
  		<tr>
   		 <th>x</th>
    	 <th>sin(x)</th>
   		 <th>cos(x)</th>
  		</tr>
  		 <c:forEach begin="0" end="${fn:length(numData) - 1}" var="i">
  		 	<tr>
   		 		<th><c:out value="${numData[i]}"/></th>
    		 	<th><c:out value="${sinData[i]}"/></th>
   		 		<th><c:out value="${cosData[i]}"/></th>
  			</tr>
  		</c:forEach>
  	</table>
</body>

</html>