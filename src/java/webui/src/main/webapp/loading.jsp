<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>M-SALSA preparing alignment</title>
	<meta name="robots" content="noindex, nofollow" />
	<jsp:include page="header.jsp" />	
</head>
<body>
	<div class="container">
		<jsp:include page="navbar.jsp" />
	
		<h1>LOADING...</h1>
		
		<div class="progress">
			<div class="progress-bar progress-bar-striped active" role="progressbar" style="width:100%">
			</div>
		</div>

		<jsp:include page="footer.jsp" />
		<jsp:include page="standard-js.jsp" />
		<script src="js/loading.js" type="text/javascript"></script>
	</div>
	<!-- Container -->
</body>
</html>