<%@page import="com.salsaw.msalsa.cli.SalsaAlgorithmExecutor"%>

<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="index"><%=SalsaAlgorithmExecutor.M_SALSA_HEADER%></a>
			<button data-target="#navbar-main" data-toggle="collapse"
				type="button" class="navbar-toggle">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>
		<div id="navbar-main" class="navbar-collapse collapse">
			<ul class="nav navbar-nav">
				<li><a href="http://salsa-w.github.io/M-SALSA/">Documentation</a></li>
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