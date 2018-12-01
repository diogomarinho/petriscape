package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

@SuppressWarnings("all")
public class CyNodeLabel {
	
	PassthroughMapping passthroughMapping;
	
	VisualMappingFunctionFactory pVisualMappingFunctionFactory;
	
	public CyNodeLabel(VisualMappingFunctionFactory pVisualMappingFunctionFactory) {
		
		this.pVisualMappingFunctionFactory = pVisualMappingFunctionFactory;
		
		
		passthroughMapping = (PassthroughMapping) this.pVisualMappingFunctionFactory.
				createVisualMappingFunction("name", String.class, BasicVisualLexicon.NODE_LABEL);
	}
	
	
	public PassthroughMapping createNodeLabel() {
		
		return this.passthroughMapping;
		
	}
}
