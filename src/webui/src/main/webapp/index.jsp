<%@page import="com.salsaw.msalsa.algorithm.MatrixSerie"%>
<%@page import="com.salsaw.msalsa.cli.SalsaAlgorithmExecutor"%>
<%@page import="com.salsaw.msalsa.clustal.ClustalType"%>
<%@page import="com.salsaw.msalsa.algorithm.TerminalGAPsStrategy"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>M-SALSA multiple sequence alignments</title>
<meta name="robots" content="index, nofollow" />
<jsp:include page="header.jsp" />
<link rel="stylesheet" href="css/btn-file.css">
<link rel="stylesheet" href="css/header.css">
</head>
<body>
	<!-- Style theme: https://bootswatch.com/superhero/ -->
	<jsp:useBean id="salsaParameters"
		class="com.salsaw.msalsa.cli.SalsaParameters" scope="request">
	</jsp:useBean>

	<c:set var="matrixSeries" value="<%=MatrixSerie.values()%>" />
	<c:set var="clustalTypes" value="<%=ClustalType.values()%>" />
	<c:set var="terminalGAPsStrategies"
		value="<%=TerminalGAPsStrategy.values()%>" />

	<div class="container">
		<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand" href="index.jsp"><%=SalsaAlgorithmExecutor.M_SALSA_HEADER%></a>
					<button data-target="#navbar-main" data-toggle="collapse"
						type="button" class="navbar-toggle">
						<span class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
				</div>
				<div id="navbar-main" class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li><a href="">Documentation</a></li>
					</ul>

					<ul class="nav navbar-nav navbar-right">
						<li><a target="_blank" href="">Contacts</a></li>
					</ul>

				</div>
			</div>
		</nav>

		<div class="jumbotron">
			<h1><%=SalsaAlgorithmExecutor.M_SALSA_HEADER%></h1>
			<p>Multiple Sequence Alignment by Local Search Algorithm</p>
		</div>

		<div class="page-header">
			<%=SalsaAlgorithmExecutor.M_SALSA_HEADER%>
			is a new multiple sequence alignment program thought to generate
			alignments between three or more sequences. For the alignment of two
			sequences please instead use other <a
				href="http://www.ebi.ac.uk/Tools/psa/">pairwise sequence
				alignment tools</a>.
		</div>


		<div class="alert alert-danger alert-dismissible collapse col-sm-12"
			role="alert" id="validation-errors">
			<button type="button" class="close" data-dismiss="alert"
				aria-label="Close">
				<span aria-hidden="true">&times;</span>
			</button>
			<strong>Error!</strong>
			<p id="validation-errors-message"></p>
		</div>

		<form id="salsa-parameters-form" class="form-horizontal" role="form"
			action="AlignmentRequestServlet" method="post"
			enctype="multipart/form-data" data-parsley-validate>

			<div class="form-group">
				<label class="control-label col-sm-2" for="inputText">Text
					to align:</label>
				<div class="col-sm-10">
					<textarea class="form-control" rows="5" id="inputText"
						name="inputText" required data-parsley-errors-messages-disabled></textarea>
				</div>
			</div>

			<div class="form-group">
				<label class="control-label col-sm-2" for="inputFile">Input
					file:</label>
				<div class="col-sm-10">
					<!-- http://stackoverflow.com/questions/11235206/twitter-bootstrap-form-file-element-upload-button -->
					<span class="btn btn-default btn-file">File to align<input
						type="file" id="inputFile" name="inputFile" required
						data-parsley-errors-messages-disabled
						onchange='$("#upload-file-info").html($(this).val());'>
					</span> &nbsp; <span class='label label-info' id="upload-file-info"></span>
				</div>
			</div>

			<div class="panel-group" id="accordion" role="tablist"
				aria-multiselectable="true">
				<div class="panel panel-default">
					<div class="panel-heading" role="tab" id="headingOne">
						<h4 class="panel-title">
							<a role="button" data-toggle="collapse" data-parent="#accordion"
								href="#collapseOne" aria-expanded="true"
								aria-controls="collapseOne">Set Advanced Options</a>
						</h4>
						<small> The default settings will
							fulfill the needs of most users and, for that reason, are not
							shown.</small>
					</div>
					<div id="collapseOne" class="panel-collapse collapse"
						role="tabpanel" aria-labelledby="headingOne">
						<div class="panel-body">

							<div class="form-group">
								<label class="control-label col-sm-2" for="clustalType">Clustal:</label>
								<div class="col-sm-10">
									<select id="clustalType" name="clustalType"
										class="form-control">
										<c:forEach items="${clustalTypes}" var="clustalType">
											<option value="${clustalType}"
												${salsaParameters.clustalType == clustalType ? 'selected' : ''}>${clustalType}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="GOP">GOP:</label>
								<div class="col-sm-10">
									<input type="number" step="any" class="form-control" id="GOP"
										name="GOP" value="${fn:escapeXml(salsaParameters.GOP)}"
										type="range" data-parsley-range="[0, 20]" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2" for="GEP">GEP:</label>
								<div class="col-sm-10">
									<input type="number" step="any" class="form-control" id="GEP"
										name="GEP" value="${fn:escapeXml(salsaParameters.GEP)}"
										type="range" data-parsley-range="[0, 20]">
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-sm-2" for="gamma">Gamma:</label>
								<div class="col-sm-10">
									<input type="number" class="form-control" id="gamma"
										name="gamma" value="${fn:escapeXml(salsaParameters.gamma)}">
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="terminalGAPsStrategy">Scoring
									Matrix Serie:</label>
								<div class="col-sm-10">
									<select id="matrixSerie" name="matrixSerie"
										class="form-control">
										<c:forEach items="${matrixSeries}" var="matrixSerie">
											<option value="${matrixSerie}"
												${salsaParameters.matrixSerie == matrixSerie ? 'selected' : ''}>${matrixSerie}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="minIterations">Min
									Iterations:</label>
								<div class="col-sm-10">
									<input type="number" class="form-control" id="minIterations"
										name="minIterations"
										value="${fn:escapeXml(salsaParameters.minIterations)}">
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="probabilityOfSplit">Probability
									Of Split:</label>
								<div class="col-sm-10">
									<input type="number" step="0.01" class="form-control"
										id="probabilityOfSplit" name="probabilityOfSplit"
										value="${fn:escapeXml(salsaParameters.probabilityOfSplit)}">
								</div>
							</div>

							<div class="form-group">
								<label class="control-label col-sm-2" for="terminalGAPsStrategy">Terminal
									GAPs Strategy:</label>
								<div class="col-sm-10">
									<select id="terminalGAPsStrategy" name="terminalGAPsStrategy"
										class="form-control">
										<c:forEach items="${terminalGAPsStrategies}"
											var="terminalGAPsStrategy">
											<option value="${terminalGAPsStrategy}"
												${salsaParameters.terminalGAPsStrategy == terminalGAPsStrategy ? 'selected' : ''}>${terminalGAPsStrategy}</option>
										</c:forEach>
									</select>
								</div>
							</div>

							<div class="form-group">
								<div class="col-sm-offset-2 col-sm-10">
									<div class="checkbox">
										<label> <c:choose>
												<c:when
													test="${salsaParameters.generatePhylogeneticTree==true}">
													<input type="checkbox" id="generatePhylogeneticTree"
														name="generatePhylogeneticTree" checked />
												</c:when>
												<c:otherwise>
													<input type="checkbox" id="generatePhylogeneticTree"
														name="generatePhylogeneticTree" unchecked />
												</c:otherwise>
											</c:choose> Generate Phylogenetic Tree
										</label>
									</div>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label class="control-label col-sm-2" for="email">Email:</label>
				<div class="col-sm-10">
					<input type="email" class="form-control" id="recipientEmail"
						name="recipientEmail" data-parsley-trigger="keyup change"
						placeholder="Enter email">
					<p class="text-muted">Add your email address if you want to be notified
						by email when the results are available</p>
				</div>
			</div>

			<div class="form-group hidden" id="userJobTitleForm">
				<label class="control-label col-sm-2" for="email">Job title:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" id="userJobTitle"
						name="userJobTitle"
						placeholder="If available, the title will be included in the subject of the notification email and can be used as a way to identify your analysis">
				</div>
			</div>

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="submit" class="btn btn-default validate" />
				</div>
			</div>

		</form>

		<jsp:include page="footer.jsp" />
		<jsp:include page="standard-js.jsp" />
		<script src="js/index.js" type="text/javascript"></script>
		<script src="js/header.js" type="text/javascript"></script>
	</div>
	<!-- Container -->
</body>
</html>
