<%@page import="com.salsaw.msalsa.config.ConfigurationManager"%>
<%@page import="com.salsaw.msalsa.cli.SalsaAlgorithmExecutor"%>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	
	<meta name="language" content="English">  
	<meta name="description" content="<%=SalsaAlgorithmExecutor.M_SALSA_HEADER%> is a multiple sequence alignment program for proteins. It produces biologically meaningful multiple sequence alignments of divergent sequences. Evolutionary relationships can be seen via viewing Cladograms or Phylograms.">
	<meta name="keywords" content="bioinformatics, multiple sequence alignment, CRIBI Biotechnology Center, unipd" />
	 	
	<meta name="author" content="Alessandro Daniele, Fabio Cesarato, Andrea Giraldin">
	<link rel="author" href="<%=SalsaAlgorithmExecutor.AUTHOR_LINK_ALESSANDRO_DANIELE%>"/>
	<link rel="author" href="<%=SalsaAlgorithmExecutor.AUTHOR_LINK_FABIO_CESARATO%>"/>
	<link rel="author" href="<%=SalsaAlgorithmExecutor.AUTHOR_LINK_ANDREA_GIRALDIN%>"/>
	
	<%
	// Set publisher only if set in configuration file
	if (ConfigurationManager.getInstance().getServerConfiguration().getSitePublisher() != null){ %>
		<link rel="publisher" href="<%=ConfigurationManager.getInstance().getServerConfiguration().getSitePublisher()%>"/>
	<%}%>
	<%-- Bootstrap 3 --%>
	<%-- Latest compiled and minified CSS --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
	
	<%-- Optional theme --%>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
	 
	<!-- Validation style -->
	<link rel="stylesheet" href="http://parsleyjs.org/src/parsley.css">
