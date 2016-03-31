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
			
		<div class="spacer"></div>
		<div class="row">
			<div  class="col-md-6">
				<form method="post" class="form-horizontal" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
					<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE%>" value="<%=AlignmentResultFileType.Alignment.toString()%>"/>
					<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
					
					<div class="text-center"> 
						<input type="submit" class="btn btn-default" value="Download Alignment" />
					</div>
				</form>
			</div>
			
			<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
				<div  class="col-md-6">
												
					<form method="post" action="<%=AlignmentResultServlet.class.getSimpleName()%>">
						<input type="hidden" name="<%=AlignmentResultServlet.FILE_TYPE_DOWNLOAD_ATTRIBUTE %>" value="<%=AlignmentResultFileType.PhylogeneticTree.toString()%>"/>
						<input type="hidden" name="<%=AlignmentStatusServlet.ID_PARAMETER%>" value="<%=request.getAttribute(AlignmentStatusServlet.ID_PARAMETER)%>"/>
						<div class="text-center"> 
							<input type="submit" class="btn btn-default" value="Download Phylogenetic Tree" />
						</div>
					</form>					
					
				</div>
			</c:if>
		</div>

		<button id="colorsButton" class="btn btn-default">Show colors</button>
		<div class="row">
			<%
				// Print all alignment data
			%>
			<pre id="sequencesNames" class="col-sm-2"><c:forEach items="${alignmentSequencesHeaders}" var="sequenceHeader">${sequenceHeader}<%=Constants.NEW_LINE%></c:forEach>
			</pre>
			<pre id="sequencesContent" class="col-sm-10"><c:forEach items="${alignmentSequencesContent}" var="sequence">${sequence}<%=Constants.NEW_LINE%></c:forEach>
			</pre>
		</div>

		<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
			<div class="spacer"></div>

			<div class="col-md-12 text-center">
				<div id="svgCanvas"></div>
			</div>

		</c:if>

		<jsp:include page="footer.jsp"/>
		<jsp:include page="standard-js.jsp"/>
		
		<!-- Load TypeScript compilation output -->
		<script src="tcs.js/amino_colors.js" type="text/javascript"></script>
		
		<c:if test="${requestScope.phylogeneticTreeDataAvailable}">
			<script type="text/javascript" src="js/raphael/raphael-min.js" ></script> 
			<script type="text/javascript" src="js/jsphylosvg/jsphylosvg-min.js"></script> 	
			<script type="text/javascript">
				var newickTreeString = '<%=request.getAttribute("newickTree")%>';
				if (newickTreeString != null){
					window.onload = function(){
						var dataObject = { newick: newickTreeString };
						phylocanvas = new Smits.PhyloCanvas(
							dataObject,
							'svgCanvas', 
							500, 500
						);
					};
				}
			</script>
		</c:if>		
		
	</div> <!-- Container -->
</body>
</html>