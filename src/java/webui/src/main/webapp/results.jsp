<%@page import="com.salsaw.msalsa.algorithm.Constants"%>
<%@page import="com.salsaw.msalsa.servlets.AlignmentStatusServlet"%>
<%@page import="com.salsaw.msalsa.servlets.AlignmentResultServlet"%>
<%@ page import="com.salsaw.msalsa.datamodel.AlignmentResultFileType"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>M-SALSA alignment results</title>
	<meta name="robots" content="noindex, nofollow" />
	<jsp:include page="header.jsp" />
	<link rel="stylesheet" href="css/aa-colors.css">
</head>
<body>
	
	<!-- http://stackoverflow.com/questions/10982153/servlet-pdf-download-button-creation -->
	<div class="container">
		<jsp:include page="navbar.jsp" />

		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#alignment">Alignment</a></li>
			<li><a data-toggle="tab" href="#tree">Phylogenetic Tree</a></li>
			<li class="downloads">
				Downloads:
					<form method="post" class="inline" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
						<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE%>" value="<%=AlignmentResultFileType.Alignment.toString()%>"/>
						<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
						
						<button type="submit" class="btn btn-default" value="" /><span class="glyphicon glyphicon-download-alt"> Alignment</span>
					</form>
				<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
						<form class="inline" method="post" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
							<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE %>" value="<%=AlignmentResultFileType.PhylogeneticTree.toString()%>"/>
							<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
							
							<button type="submit" class="btn btn-default" value="" /><span class="glyphicon glyphicon-download-alt"> Phylogenetic Tree</span>
						</form>
				</c:if>
			</li>
		</ul>

		<div class="tab-content">
			<div id="alignment" class="tab-pane fade in active">
				<button id="colorsButton" class="btn btn-default">Show colors</button>
				<div class="row correct-margin">
					<%
						// Print all alignment data
					%>
					<pre id="sequencesNames" class="col-sm-2"><c:forEach items="${alignmentSequencesHeaders}" var="sequenceHeader">${sequenceHeader}<%=Constants.NEW_LINE%></c:forEach></pre>
					<pre id="sequencesContent" class="col-sm-10"><c:forEach items="${alignmentSequencesContent}" var="sequence">${sequence}<%=Constants.NEW_LINE%></c:forEach></pre>
				</div>
			</div>
			
			<div id="tree" class="tab-pane fade">
				<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
					<div class="spacer"></div>
		
					<div class="col-md-12 text-center">
						<div id="svgCanvas"></div>
					</div>
				</c:if>
			</div>
		</div>

		<jsp:include page="footer.jsp"/>
		<jsp:include page="standard-js.jsp"/>
		
		<!-- Load TypeScript compilation output -->
		<script src="tcs.js/amino_colors.js" type="text/javascript"></script>
		
		<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
			<script type="text/javascript" src="js/raphael/raphael-min.js" ></script> 
			<script type="text/javascript" src="js/jsphylosvg/jsphylosvg-min.js"></script> 	
			<script type="text/javascript">			
				var newickTreeString = '<%=request.getAttribute("newickTree")%>';
				var sequencesNumber = ${alignmentSequencesNumber};
				if (newickTreeString != null){
					window.onload = function(){
						var dataObject = { newick: newickTreeString };
						phylocanvas = new Smits.PhyloCanvas(
							dataObject,
							'svgCanvas', 
							1100, 27 * sequencesNumber
						);
						document.getElementById('tree').setAttribute("style","height:500px");
					};
				}
			</script>
		</c:if>		
		
	</div> <!-- Container -->
</body>
</html>