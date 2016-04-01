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
if (ConfigurationManager.getInstance().getServerConfiguration().getSitePublisher().isEmpty() == false){ %>
	<link rel="publisher" href="<%=ConfigurationManager.getInstance().getServerConfiguration().getSitePublisher()%>"/>
<%}%>

<%
// Set publisher Google Analytics script only if ID present
if (ConfigurationManager.getInstance().getServerConfiguration().getGoogleAnalyticsPropertyID().isEmpty() == false) { %>
	<script>
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
		(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		
		ga('create', '<%=ConfigurationManager.getInstance().getServerConfiguration().getGoogleAnalyticsPropertyID()%>', 'auto');
		ga('send', 'pageview');
	</script>
<%} // end Google Analytics%>

<%-- Bootstrap 3 --%>
<%-- Latest compiled and minified CSS --%>
<link rel="stylesheet" href="https://bootswatch.com/lumen/bootstrap.min.css">

<%-- Optional theme --%>
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"> -->

<!-- Validation style -->
<link rel="stylesheet" href="http://parsleyjs.org/src/parsley.css">

<!-- Common styles -->
<link rel="stylesheet" href="css/btn-file.css">
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/spacer.css">
<link rel="stylesheet" href="css/corner-ribbon.css">
<link rel="stylesheet" href="css/info-button.css">
<link rel="stylesheet" href="css/tabs.css">
