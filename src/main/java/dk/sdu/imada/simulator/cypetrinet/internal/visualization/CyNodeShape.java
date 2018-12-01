package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.NodeShapeVisualProperty;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

import dk.sdu.imada.simulator.cypetrinet.internal.util.PetriNetUtil;

@SuppressWarnings("all")
public class CyNodeShape {
	
//	CyNetwork cyNetwork;
	
	DiscreteMapping discreteMapping;
	
	VisualMappingFunctionFactory dVisualMappingFunctionFactory;
	
	public CyNodeShape(VisualMappingFunctionFactory dVisualMappingFunctionFactory) {
		
		this.dVisualMappingFunctionFactory = dVisualMappingFunctionFactory;
		
		this.discreteMapping =  (DiscreteMapping) this.dVisualMappingFunctionFactory.
				createVisualMappingFunction("type", String.class, BasicVisualLexicon.NODE_SHAPE);
	}
	
	public DiscreteMapping createNodeShape() {
		
		discreteMapping.putMapValue("place", NodeShapeVisualProperty.ELLIPSE);
		
		discreteMapping.putMapValue("transition", NodeShapeVisualProperty.RECTANGLE);
		
		return discreteMapping;
	}
}
