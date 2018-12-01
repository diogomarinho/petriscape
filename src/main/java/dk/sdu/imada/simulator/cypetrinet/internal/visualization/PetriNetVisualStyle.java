package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import java.util.Set;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.VisualStyleFactory;

public class PetriNetVisualStyle {
	
	VisualStyleFactory visualStyleFactory;
	
	VisualMappingManager visualMappingManager;
	
//	VisualPropertyDependency visualPropertyDependency;
	
	VisualMappingFunctionFactory dVisualMappingFunctionFactory;
	
	VisualMappingFunctionFactory pVisualMappingFunctionFactory;

	VisualMappingFunctionFactory cVisualMappingFunctionFactory;
	
	VisualStyle vs;

	
	public PetriNetVisualStyle(	
								VisualStyleFactory visualStyleFactory,
//								VisualPropertyDependency visualPropertyDependency,
								VisualMappingManager visualMappingManager,
								VisualMappingFunctionFactory dVisualMappingFunctionFactory,
								VisualMappingFunctionFactory pVisualMappingFunctionFactory,
								VisualMappingFunctionFactory cVisualMappingFunctionFactory) {
		
		
		this.visualStyleFactory = visualStyleFactory;
		
		this.visualMappingManager = visualMappingManager;
		
		this.dVisualMappingFunctionFactory = dVisualMappingFunctionFactory;
		
		this.pVisualMappingFunctionFactory = pVisualMappingFunctionFactory;
		
		this.cVisualMappingFunctionFactory =  cVisualMappingFunctionFactory;
		
		generateVisualStyle();
		
	}
	
//	. creates the visualStyle but without the colors for nodes
	public void generateVisualStyle() {
		
		for (VisualStyle vs : visualMappingManager.getAllVisualStyles()) {
			
			if (vs.getTitle().equalsIgnoreCase("petriNetVS")) {
				
				visualMappingManager.removeVisualStyle(vs);
			}
		}

		vs = this.visualStyleFactory.createVisualStyle("petriNetVS");
		
		visualMappingManager.addVisualStyle(vs);
		
		
		CyNodeShape cyNodeShape = new CyNodeShape(dVisualMappingFunctionFactory);
		
		CyEdgeShape cyEdgeShape = new CyEdgeShape(cVisualMappingFunctionFactory);
		
		CyNodeLabel cyNodeLabel = new CyNodeLabel(pVisualMappingFunctionFactory);
		
		CyNodeDefaultColor cyNodeDefaultColor = new CyNodeDefaultColor(cVisualMappingFunctionFactory);
	
		
		vs.addVisualMappingFunction(cyNodeShape.createNodeShape());
		
		vs.addVisualMappingFunction(cyEdgeShape.createCyEdgeShape());
		
		vs.addVisualMappingFunction(cyNodeLabel.createNodeLabel());
		
		vs.addVisualMappingFunction(cyNodeDefaultColor.createDefaultColor());
	}
	
//	. 
	public void setPetriNetVisualStyleAsCurrent(CyNetworkView cyNetworkView ) {
		
		VisualStyle vs = getPetriNetVisualStyle();
		
		this.visualMappingManager.setVisualStyle(vs, cyNetworkView);
		
		this.visualMappingManager.setCurrentVisualStyle(vs);
		
		vs.apply(cyNetworkView);
	}
	
	
	public void colorizeNodes(CyNetwork cyNetwork, CyNetworkView cyNetworkView) {
		
		if (cyNetwork.getNodeCount() > 0 && cyNetwork!=null) {
			
			CyNodeGradientColor cyNodeGradientColor = new CyNodeGradientColor(cyNetwork, dVisualMappingFunctionFactory);
			
			vs.addVisualMappingFunction(cyNodeGradientColor.createPlaceGradient());
			
			vs.addVisualMappingFunction(cyNodeGradientColor.createTransitionGradient());
			
			vs.apply(cyNetworkView);
			
			cyNetworkView.updateView();
		}
	}
	
//	.
	public void setDefaultColorNode() {
		
		VisualStyle vs = getPetriNetVisualStyle();
		
		CyNodeDefaultColor cyNodeDefaultColor = new CyNodeDefaultColor(cVisualMappingFunctionFactory);

		vs.addVisualMappingFunction(cyNodeDefaultColor.createDefaultColor());
	}

//	. 
	private VisualStyle getPetriNetVisualStyle() {
		
		Set<VisualStyle> list = this.visualMappingManager.getAllVisualStyles();
		
		for (VisualStyle vs : list) {
			
			if (vs.getTitle().equalsIgnoreCase("petriNetVS" )) {
				return vs;
			}
		}
		
		return null;
	}
	
//	. 
	public boolean existsPetriNetVS() {
//		String title = "petriNetVS";
		
		for (VisualStyle vs : visualMappingManager.getAllVisualStyles()) {
			
			if (vs.getTitle().equalsIgnoreCase("petriNetVS" )) {
				
				visualMappingManager.removeVisualStyle(vs);
				
				return true;
			}
		}
		
		return false;
	}
	
	public void updateVisualStyle(VisualStyle vs) {
		
		this.vs = vs;
	}
}
