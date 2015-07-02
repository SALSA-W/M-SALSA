<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	
	<%-- Bootstrap 3 --%>
	<%-- Latest compiled and minified CSS --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	
	<%-- Optional theme --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
	
	<script type="text/javascript" src="js/raphael-min.js" ></script> 
	<script type="text/javascript" src="js/jsphylosvg-min.js"></script> 
	
	<script type="text/javascript">
		window.onload = function(){
				var dataObject = { newick: '<%= request.getAttribute("newickTree") %>' };
				phylocanvas = new Smits.PhyloCanvas(
					dataObject,
					'svgCanvas', 
					500, 500
				);
		};
	</script>
</head>
<body>	
	<div id="svgCanvas"> </div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <%-- 
	    Latest compiled and minified JavaScript 
	    Include all compiled plugins (below), or include individual files as needed
    --%>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>