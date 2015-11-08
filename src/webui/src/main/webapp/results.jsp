<%@page import="com.salsaw.msalsa.servlets.AlignmentStatusServlet"%>
<%@page import="com.salsaw.msalsa.servlets.AlignmentResultServlet"%>
<%@ page import="com.salsaw.msalsa.datamodel.AlignmentResultFileType"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>M-SALSA alignment results</title>
	<meta name="robots" content="noindex, nofollow" />
	<jsp:include page="header.jsp" />
	
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
	<!-- http://stackoverflow.com/questions/10982153/servlet-pdf-download-button-creation -->
	<div class="container">
		<form method="post" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
			<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE %>" value="<%=AlignmentResultFileType.Alignment.toString()%>"/>
			<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
			
			<input type="submit" value="Download Alignment" />
		</form>
		
		<form method="post" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
			<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE %>" value="<%=AlignmentResultFileType.PhylogeneticTree.toString()%>"/>
			<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
			
			<input type="submit" value="Download Phylogenetic Tree" />
		</form>
		
		<div id="svgCanvas" />

		<jsp:include page="footer.jsp"/>
		<jsp:include page="standard-js.jsp"/>
	</div> <!-- Container -->
</body>
</html>