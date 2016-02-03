<!-- http://stackoverflow.com/a/2748251 -->
<!-- http://www.tutorialspoint.com/jsp/jsp_exception_handling.htm -->
<%@page isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>M-SALSA multiple sequence alignments</title>
	<meta name="robots" content="noindex, nofollow" />
	<jsp:include page="header.jsp" />
</head>
<body>
	<div class="container">
		<jsp:include page="navbar.jsp" />
		
		<h1>Opps...</h1>
		<table width="100%" border="1">
			<tr valign="top">
				<td width="40%"><b>Error:</b></td>
				<td>${pageContext.exception}</td>
			</tr>
			<tr valign="top">
				<td><b>URI:</b></td>
				<td>${pageContext.errorData.requestURI}</td>
			</tr>
			<tr valign="top">
				<td><b>Status code:</b></td>
				<td>${pageContext.errorData.statusCode}</td>
			</tr>
			<tr valign="top">
				<td><b>Stack trace:</b></td>
				<td><c:forEach var="trace"
						items="${pageContext.exception.stackTrace}">
						<p>${trace}</p>
					</c:forEach></td>
			</tr>
		</table>
		
		<jsp:include page="footer.jsp" />
		<jsp:include page="standard-js.jsp" />
	</div>
	<!-- Container -->
</body>
</html>