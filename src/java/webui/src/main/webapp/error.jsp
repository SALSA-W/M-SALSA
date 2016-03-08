<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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
		<c:if test="${not empty errorMessage}">

			<div class="alert alert-danger col-sm-12">
				<strong>Error!</strong>
				<p>
					<c:out value="${errorMessage}" />
				</p>
			</div>

		</c:if>
	</div>
	<!-- Container -->
</body>
</html>