<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<jsp:include page="header.jsp" />
	<title>Loading</title>
	<script src="js/loading.js" type="text/javascript"></script>
</head>
<body>
	<div class="container">
		<h1>LOADING...</h1>
		
		<div class="progress">
			<div class="progress-bar progress-bar-striped active" role="progressbar" style="width:100%">
			</div>
		</div>

		<jsp:include page="footer.jsp" />
		<jsp:include page="standard-js.jsp" />
	</div>
	<!-- Container -->
</body>
</html>