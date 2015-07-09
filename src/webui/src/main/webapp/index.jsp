<%@page import="com.salsaw.msalsa.cli.ScoringMatrix"%>
<%@page import="com.salsaw.msalsa.clustal.ClustalType"%>
<%@page import="com.salsaw.msalsa.algorithm.TerminalGAPsStrategy"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
 <%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	<meta name="author" content="Alessandro Daniele, Fabio Cesarato, Andrea Giraldin">
	<title>M-SALSA</title>	
	
	<%-- Bootstrap 3 --%>
	<%-- Latest compiled and minified CSS --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	
	<%-- Optional theme --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">	
</head>
<body>

	<div class="container">
	
	<h1 class="text-center">M-SALSA</h1>
	
	<jsp:useBean id="salsaParameters" class="com.salsaw.msalsa.cli.SalsaParameters" scope="request">
	</jsp:useBean>
	 
	<c:set var="scoringMatrixes" value="<%=ScoringMatrix.values()%>"/>
	<c:set var="clustalTypes" value="<%=ClustalType.values()%>"/>
	<c:set var="terminalGAPsStrategies" value="<%=TerminalGAPsStrategy.values()%>"/>
	
	 <form class="form-horizontal" role="form" action="AlignmentRequestServlet" method="post" enctype="multipart/form-data">
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="email">Email:</label>
		    <div class="col-sm-10">
		      <input type="email" class="form-control" id="email" name="email" placeholder="Enter email">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="inputFile">Input file:</label>
		    <div class="col-sm-10">
		      <input type="file" id="inputFile" name="inputFile" placeholder="The file to align" />
		    </div>
		  </div>
		  
		 <div class="form-group">
		  	 <label class="control-label col-sm-2" for="clustalType">Clustal:</label>
			 <div class="col-sm-10">
				<select id="clustalType" name="clustalType" class="form-control">
				    <c:forEach items="${clustalTypes}" var="clustalType">
				        <option value="${clustalType}" ${salsaParameters.clustalType == clustalType ? 'selected' : ''}>${clustalType}</option>
				    </c:forEach>
				</select>
			</div>
		</div>		  
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="GOP">GOP:</label>
		    <div class="col-sm-10">
		      <input type="number" step="any" class="form-control" id="GOP" name="GOP" value="${fn:escapeXml(salsaParameters.GOP)}" placeholder="Enter password" />
		    </div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="GEP">GEP:</label>
		    <div class="col-sm-10">
		      <input type="number" step="any" class="form-control" id="GEP" name="GEP" value="${fn:escapeXml(salsaParameters.GEP)}" placeholder="Enter password">
		    </div>
		  </div>
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="gamma">Gamma:</label>
		    <div class="col-sm-10">
		      <input type="number" class="form-control" id="gamma" name="gamma" value="${fn:escapeXml(salsaParameters.gamma)}" placeholder="Enter password">
		    </div>
		  </div>
		  
		 <div class="form-group">
		  	 <label class="control-label col-sm-2" for="terminalGAPsStrategy">Scoring Matrix:</label>
			 <div class="col-sm-10">
				<select id="scoringMatrix" name="scoringMatrix" class="form-control">
				    <c:forEach items="${scoringMatrixes}" var="scoringMatrix">
				        <option value="${scoringMatrix}" ${salsaParameters.scoringMatrix == scoringMatrix ? 'selected' : ''}>${scoringMatrix}</option>
				    </c:forEach>
				</select>
			</div>
		</div>
		  
		 <div class="form-group">
		    <label class="control-label col-sm-2" for="minIterations">Min Iterations:</label>
		    <div class="col-sm-10">
		      <input type="number" class="form-control" id="minIterations" name="minIterations" value="${fn:escapeXml(salsaParameters.minIterations)}" placeholder="Enter password">
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <label class="control-label col-sm-2" for="probabilityOfSplit">Probability Of Split:</label>
		    <div class="col-sm-10">
		      <input type="number" step="0.01" class="form-control" id="probabilityOfSplit" name="probabilityOfSplit" value="${fn:escapeXml(salsaParameters.probabilityOfSplit)}" placeholder="Enter password">
		    </div>
		  </div>
		  
		  <div class="form-group">
		  	  <label class="control-label col-sm-2" for="terminalGAPsStrategy">Terminal GAPs Strategy:</label>
			  <div class="col-sm-10">					
				<select id="terminalGAPsStrategy" name="terminalGAPsStrategy" class="form-control">
				    <c:forEach items="${terminalGAPsStrategies}" var="terminalGAPsStrategy">
				        <option value="${terminalGAPsStrategy}" ${salsaParameters.terminalGAPsStrategy == terminalGAPsStrategy ? 'selected' : ''}>${terminalGAPsStrategy}</option>
				    </c:forEach>
				</select>					
				</div>
			</div>

		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <div class="checkbox">
		        <label>
		        <c:choose>
		            <c:when test="${salsaParameters.generatePhylogeneticTree==true}">
		            	<input type="checkbox" id="generatePhylogeneticTree" name="generatePhylogeneticTree" checked/>
		            </c:when>
		            <c:otherwise>
		            	<input type="checkbox" id="generatePhylogeneticTree"  name="generatePhylogeneticTree" unchecked/>
		            </c:otherwise>
		        </c:choose>
		        Generate Phylogenetic Tree</label>
		      </div>
		    </div>
		  </div>
		  
		  <div class="form-group">
		    <div class="col-sm-offset-2 col-sm-10">
		      <button type="submit" class="btn btn-default" >Submit</button>
		    </div>
		  </div>
		</form>
		
	</div>
	
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <%-- 
	    Latest compiled and minified JavaScript 
	    Include all compiled plugins (below), or include individual files as needed
    --%>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</body>
</html>