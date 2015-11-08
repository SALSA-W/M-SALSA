<%@page import="com.salsaw.msalsa.cli.SalsaAlgorithmExecutor"%>
<%@page import="com.salsaw.msalsa.cli.ScoringMatrix"%>
<%@page import="com.salsaw.msalsa.clustal.ClustalType"%>
<%@page import="com.salsaw.msalsa.algorithm.TerminalGAPsStrategy"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<title>M-SALSA multiple sequence alignments</title>
	<meta name="robots" content="index, nofollow" />
	<jsp:include page="header.jsp" />
</head>
<body>
	<div class="container">
		<h1 class="text-center"><%=SalsaAlgorithmExecutor.M_SALSA_HEADER%></h1>
		<jsp:useBean id="salsaParameters"
			class="com.salsaw.msalsa.cli.SalsaParameters" scope="request">
		</jsp:useBean>

		<c:set var="scoringMatrixes" value="<%=ScoringMatrix.values()%>" />
		<c:set var="clustalTypes" value="<%=ClustalType.values()%>" />
		<c:set var="terminalGAPsStrategies"
			value="<%=TerminalGAPsStrategy.values()%>" />

		<div class="alert alert-danger alert-dismissible collapse"
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
				<label class="control-label col-sm-2" for="email">Email:</label>
				<div class="col-sm-10">
					<input type="email" class="form-control" id="recipientEmail"
						name="recipientEmail" data-parsley-trigger="change"
						placeholder="Enter email">
				</div>
			</div>

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
					<input type="file" id="inputFile" name="inputFile"
						placeholder="The file to align" required
						data-parsley-errors-messages-disabled />
				</div>
			</div>

			<button class="text-center" type="button" data-toggle="collapse"
				data-target="#demo">Advanced Options</button>

			<div id="demo" class="collapse">
				<div class="form-group">
					<label class="control-label col-sm-2" for="clustalType">Clustal:</label>
					<div class="col-sm-10">
						<select id="clustalType" name="clustalType" class="form-control">
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
							type="range" data-parsley-range="[0, 20]" >
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" for="gamma">Gamma:</label>
					<div class="col-sm-10">
						<input type="number" class="form-control" id="gamma" name="gamma"
							value="${fn:escapeXml(salsaParameters.gamma)}">
					</div>
				</div>

				<div class="form-group">
					<label class="control-label col-sm-2" for="terminalGAPsStrategy">Scoring
						Matrix:</label>
					<div class="col-sm-10">
						<select id="scoringMatrix" name="scoringMatrix"
							class="form-control">
							<c:forEach items="${scoringMatrixes}" var="scoringMatrix">
								<option value="${scoringMatrix}"
									${salsaParameters.scoringMatrix == scoringMatrix ? 'selected' : ''}>${scoringMatrix}</option>
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
							<label>
								<c:choose>
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

			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-10">
					<input type="submit" class="btn btn-default validate" />
				</div>
			</div>
		</form>

		<jsp:include page="footer.jsp" />
		<jsp:include page="standard-js.jsp" />
		<script src="js/index.js" type="text/javascript"></script>
	</div>
	<!-- Container -->
</body>
</html>
