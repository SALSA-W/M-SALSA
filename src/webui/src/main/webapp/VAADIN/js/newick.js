com_salsaw_msalsa_JsPhyloSVG = function() {	
		
	var state = this.getState();
	
	phyloCanvas = new Smits.PhyloCanvas(
		{ newick: state.newickTree },
		'svgCanvas', 
		500, 500
	);
}