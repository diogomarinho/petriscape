package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;

import dk.sdu.imada.simulator.cypetrinet.internal.util.CyNodeTokenComparator;
import dk.sdu.imada.simulator.cypetrinet.internal.util.PetriNetUtil;

@SuppressWarnings("all")
public class CyNodeGradientColor {
	
	CyNetwork cyNetwork;
	
	VisualMappingFunctionFactory dVisualMappingFunctionFactory;
	
	DiscreteMapping discreteMapping;
	
	public CyNodeGradientColor(CyNetwork cyNetwork, VisualMappingFunctionFactory dVisualMappingFunctionFactory) {
		
		this.cyNetwork = cyNetwork;

		this.dVisualMappingFunctionFactory = dVisualMappingFunctionFactory;
		
		// ... discrete mapping function for the cyNodes coloring .
		discreteMapping = (DiscreteMapping) this.dVisualMappingFunctionFactory.
				createVisualMappingFunction("SUID", Long.class, BasicVisualLexicon.NODE_FILL_COLOR); 
	}
	
	// colorizing places from white ... to Red
	public DiscreteMapping createPlaceGradient() {
	
		ArrayList<CyNode> places = PetriNetUtil.getPlaces(cyNetwork);

		ArrayList<Color> colors = createRedGradient();
		
		ArrayList<Integer> marking = PetriNetUtil.getMarkingVector(cyNetwork);
		
		int max = Collections.max(marking); // the max number of tokens in a single place .
		
		for (CyNode place : places) {
			
			if (max == 0 ){
				
				discreteMapping.putMapValue(place.getSUID(), Color.WHITE);
				
			}else  {
				
				int referenceValue = PetriNetUtil.getToken(cyNetwork, place);
				
				discreteMapping.putMapValue(place.getSUID(), getColor(referenceValue, max, colors));
			}
		}

		return discreteMapping;
	}

//	. colorizing transtion based on the transition activity (firing) from white to green
	public DiscreteMapping createTransitionGradient() {
		
		ArrayList<CyNode> transitions = PetriNetUtil.getTransitions(cyNetwork);
		
		ArrayList<Color> colors = createGreenGradient();
		
		ArrayList<Integer> transitionActivityVec = PetriNetUtil.getTransitionActivityList(cyNetwork);
		
		int max = Collections.max(transitionActivityVec); // . The max number of tokens in a single place.
		
		for (CyNode transition : transitions) {
			
			if (max == 0 ){
				
				discreteMapping.putMapValue(transition.getSUID(), Color.WHITE);
				
			}else  {
				
				int referenceValue = PetriNetUtil.getTransitionActivity(cyNetwork, transition);
				
				discreteMapping.putMapValue(transition.getSUID(), getColor(referenceValue, max, colors));
			}
		}
		
		return discreteMapping;
	}

//	. all range of red (white to red ... )
	private ArrayList<Color> createRedGradient() {
		
		ArrayList<Color> red = new ArrayList<Color>();
		
		int ref = 255;
		
		for (int i = 0; i < 256; i++) {
			 
			Color color = new Color(255, Math.abs(ref), Math.abs(ref));
			 
			 red.add(color);
			 
			 ref-=1;
		 }
		
		return red;
	}

//	. all range of greens (white to green) 
	private ArrayList<Color> createGreenGradient() {
		
		ArrayList<Color> green = new ArrayList<Color>();
		
		int ref = 255;
		
		for (int i = 0; i < 256; i++) {
			 
			 Color color = new Color(Math.abs(ref), 255, Math.abs(ref));
			 
			 green.add(color);
			 
			 ref-=1;
		 }
		 
		return green;
	}
	
	private Color getColor(int referenceValue, int max, ArrayList<Color> colors) {
		
		/* 
		 *	total --- 255
		 *	ref   ---  x
		*/
		
		int numColors = colors.size() - 1;
//		System.out.println("NUM COLORS: " + numColors);
				
		
		int normalizedValue = (numColors * referenceValue) / max; 
				
		int[] mapping = new int[colors.size()];
		
		return colors.get(normalizedValue);
	}
}
