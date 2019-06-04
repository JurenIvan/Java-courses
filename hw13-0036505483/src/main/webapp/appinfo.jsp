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

<%!private String formatThisAsTime(long milis) {
		StringBuilder sb = new StringBuilder();
		long val;
		if (milis > 24 * 60 * 60 * 1000) {
			val=milis / (24 * 60 * 60 * 1000);
			sb.append(val + val==1?" day ":" days ");
			milis = milis % (24 * 60 * 60 * 1000);
		}
		if (milis > 60 * 60 * 1000) {
			val=milis / (60 * 60 * 1000);
			sb.append(val + (val==1?" hour ":" hours "));
			milis = milis % (60 * 60 * 1000);
		}

		if (milis > 60 * 1000) {
			val=milis / (60 * 1000);
			sb.append(val +( val==1?" minute ":" minutes "));
			milis = milis % (60 * 1000);
		}
		if (milis > 1000) {
			val=milis / 1000;
			sb.append(val +( val==1?" second ":" seconds "));
			milis = milis % 1000;
		}
		if (milis > 0) {
			sb.append(milis + (milis==1?" milisecond ":" miliseconds "));
		}

		return sb.toString();
	}%>

<body>
	<h1>
		This app has been running for:
		<%
		long diference = System.currentTimeMillis() - (long) request.getServletContext().getAttribute("initTime");
		out.write(formatThisAsTime(diference));
	%>
	</h1>
</body>

</html>