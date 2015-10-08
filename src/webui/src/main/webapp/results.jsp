<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<jsp:include page="header.jsp" />
	<title>Result</title>
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
	<div class="container">
		<div id="svgCanvas" />

		<jsp:include page="footer.jsp"/>
		<jsp:include page="standard-js.jsp"/>
	</div> <!-- Container -->
</body>
</html>