package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import java.awt.Color;

import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;

@SuppressWarnings("all")
public class CyNodeDefaultColor {
	
	ContinuousMapping continuousMapping;
	
	VisualMappingFunctionFactory cVisualMappingFunctionFactory;
	
	public CyNodeDefaultColor(VisualMappingFunctionFactory cVisualMappingFunctionFactory) {
		
		this.cVisualMappingFunctionFactory = cVisualMappingFunctionFactory;
		
		
		continuousMapping = (ContinuousMapping) this.cVisualMappingFunctionFactory.createVisualMappingFunction("SUID", Long.class, BasicVisualLexicon.NODE_FILL_COLOR);
		
	}
	
	public ContinuousMapping createDefaultColor() {
		
		BoundaryRangeValues<Color> brv =  new BoundaryRangeValues<Color>(Color.white, Color.white, Color.white);
		
		continuousMapping.addPoint(0, brv);
		
		return continuousMapping;
		
	}

}
