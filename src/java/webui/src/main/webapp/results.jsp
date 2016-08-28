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

		<div class="tab-content" id="results-tab">
			<div id="alignment" class="tab-pane fade in active">
				<button id="colorsButton" class="btn btn-default">Show colors</button>
				
				<h2>FASTA format aggregate result</h2>
				<div class="row correct-margin">
					<%
						// Print all alignment data
					%>
					<pre class="col-sm-2 sequencesNames"><c:forEach items="${alignmentFastaSequencesHeaders}" var="sequenceHeader">${sequenceHeader}<%=Constants.NEW_LINE%></c:forEach></pre>
					<pre class="col-sm-10 sequencesContent"><c:forEach items="${alignmentFastaSequencesContent}" var="sequence">${sequence}<%=Constants.NEW_LINE%></c:forEach></pre>
				</div>
				
				<c:if test="${not empty alignmentClustalSections}">
					<h2>CLUSTAL format result</h2>
					<c:forEach items="${alignmentClustalSections}" var="clustalFileSection">
						<div class="row correct-margin">	
							<pre class="col-sm-2 sequencesNames"><c:forEach items="${clustalFileSection.sequencesHeaders}" var="sequenceHeader">${sequenceHeader}<%=Constants.NEW_LINE%></c:forEach></pre>
							<pre class="col-sm-10 sequencesContent"><c:forEach items="${clustalFileSection.sequences}" var="sequence">${sequence}<%=Constants.NEW_LINE%></c:forEach></pre>
						</div>
					</c:forEach>
				</c:if>
				
			</div>
			
            <div id="tree" class="tab-pane fade">
                <c:if test="${requestScope.phylogeneticTreeDataAvailable}">
                    <div class="col-md-12 text-center" >
                        <div class="row">
                            <div id="canvas-container-div"></div>
                        </div>
                        <div class="row">
                            <div class="spacer"></div>
                        </div>
                        <div class="row">
                            <div id="scale-container-div"></div>
                        </div>
                    </div>
                </c:if>
			</div>
		</div>

		<jsp:include page="footer.jsp"/>
		<jsp:include page="standard-js.jsp"/>
		
		<!-- Load TypeScript compilation output -->
		<script src="tcs.js/amino_colors.js" type="text/javascript"></script>
		
		<c:if test="${requestScope.phylogeneticTreeDataAvailable}">

		<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.6/d3.min.js"></script>
		<script src="https://rawgit.com/DessimozLab/phylo-io/master/www/js/treecompare.js"></script>
		<script type="text/javascript">			
				// https://github.com/phylocanvas/phylocanvas/wiki/Quick-Start
				var newickTreeString = '${newickTree}';
				var sequencesNumber = ${alignmentSequencesNumber};
                var height = (23 * sequencesNumber) + 150; // add the space for buttons and zoom manager
				
				var treecomp = null;
				
				function drawNewickTree(){

					treecomp.viewTree("Newick Tree", "canvas-container-div", "scale-container-div");
                    // Manually set div height to ensure correct space
                    $("#canvas-container-div").height(height);
                    // Update container height to get correct shape
                    document.getElementById('tree').setAttribute("style","height:" + (height + 50) + "px");
				}

				if (newickTreeString != null) {
                    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
                        // Get the name of activated
                        var tabName = $(e.target).attr("href");
                        if (tabName === "#tree")
                        {
                            treecomp = TreeCompare.init({
                                //whether the tree is scaled to fit in the render space on initial render
                                fitTree: "scale", //none, scale
                            });
                            treecomp.addTree(newickTreeString, "Newick Tree");
                            drawNewickTree();
                        }
                    });

					window.onresize = function(event) {
                        var activeTabName = $('.nav-tabs .active > a').attr('href');
                        if (activeTabName === "#tree")
                        {
                            // Resize the tree only if the tree tab is open
                            drawNewickTree();
                        }
					};
				}
			</script>
		</c:if>		
		
	</div> <!-- Container -->
</body>
</html>
