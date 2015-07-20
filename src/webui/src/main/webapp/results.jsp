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

		<div id="svgCanvas"> </div>

<jsp:include page="footer.jsp"/>