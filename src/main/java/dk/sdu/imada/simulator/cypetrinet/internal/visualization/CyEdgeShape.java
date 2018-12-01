package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.presentation.property.ArrowShapeVisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.presentation.property.values.ArrowShape;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.cytoscape.view.vizmap.mappings.PassthroughMapping;

@SuppressWarnings("all")
public class CyEdgeShape {
	
	VisualMappingFunctionFactory cVisualMappingFunctionFactory;
	
	ContinuousMapping continousMapping;
	
	public CyEdgeShape(VisualMappingFunctionFactory cVisualMappingFunctionFactory) {
		
		this.cVisualMappingFunctionFactory = cVisualMappingFunctionFactory;
		
		// ... discrete mapping function for the cyNodes coloring .
		continousMapping = (ContinuousMapping) this.cVisualMappingFunctionFactory.
				createVisualMappingFunction("SUID", Long.class, BasicVisualLexicon.EDGE_TARGET_ARROW_SHAPE); 
	}
	
	public ContinuousMapping createCyEdgeShape() {
		
		long v = 0;
		
		BoundaryRangeValues<ArrowShape> brv = new BoundaryRangeValues<ArrowShape>(ArrowShapeVisualProperty.DELTA, ArrowShapeVisualProperty.DELTA, ArrowShapeVisualProperty.DELTA);
		
		continousMapping.addPoint(0, brv);

		return this.continousMapping;
	}
}
