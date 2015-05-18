com_salsaw_msalsa_JsPhyloSVG = function() {

	this.onStateChange = function() {
		phyloCanvas = new Smits.PhyloCanvas({
			newick : this.getState().newickTree
		}, 
		this.getState().divId, 
		this.getState().svgHeight, 
		this.getState().svgWidth);
	}
}