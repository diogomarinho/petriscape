package dk.sdu.imada.simulator.cypetrinet.internal.algorithm;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

import dk.sdu.imada.simulator.cypetrinet.internal.util.PetriNetUtil;

public class CyNetworkMapping extends CyMatrix {

//	. constructor
	public CyNetworkMapping(CyNetwork  cyNetwork) {
		super(cyNetwork);
	}

	public CyNetwork getCyNetwork() {
		return cyNetwork;
	}

	public void updateMarking () {
//		ArrayList<CyNode> transitions = getOrderedTransitions();
		ArrayList<CyNode> transitions = getFiringOrder();
		
		ArrayList<CyNode> firedTransitionsList = new ArrayList<CyNode>();
		
		for (int i = 0; i < transitions.size(); i++) {
			
			CyNode t = transitions.get(i) ;

			if (t!=null) {
				
				if (fireTranstion(t)) {
					
					PetriNetUtil.updateTransitionActivity(cyNetwork, PetriNetUtil.getTransitionActivity(cyNetwork, t) + 1, t);
					
					consumeTokens(t);
					
					firedTransitionsList.add(t);
					
					System.out.println("Firing transition " + PetriNetUtil.getNodeName(cyNetwork, t) + ".");
				}
				
			}else {
				JOptionPane.showMessageDialog(null, "Error in selecting the transitions (!) ");
			}
		}
		
		if (firedTransitionsList.size() > 0) {
			
			for (CyNode t : firedTransitionsList) {
				emitTokens(t);
			}
			
		}else {
			System.out.println("No transitions to fire (.)");
		}
	}

//	. consume tokens for the source places from a fired transition! 
	private void consumeTokens(CyNode transition) {
		
		ArrayList<CyEdge> arcs = getIncomingArcs(transition);
		
		for (CyEdge arc : arcs) {
			
			CyNode p = arc.getSource();
			
			int updateToken = PetriNetUtil.getToken(cyNetwork, p) - PetriNetUtil.getWeight(cyNetwork, arc);
			
			PetriNetUtil.updateToken(cyNetwork, updateToken, p);
		}
	}

//	. add tokens for target places from a fired transition !
	private void emitTokens(CyNode transition) {
		
		ArrayList<CyEdge> arcs = getOutgoingArcs(transition);

		for (CyEdge arc : arcs) {
			
			CyNode p = arc.getTarget();
			
			int updateToken = PetriNetUtil.getToken(cyNetwork, p) + PetriNetUtil.getWeight(cyNetwork, arc);
			
			PetriNetUtil.updateToken(cyNetwork, updateToken, p);
		}
	}
	
//	check if a transition will be fired .
	private boolean fireTranstion(CyNode transition) {
		
		boolean condition = true;
		
		ArrayList<CyNode> sourceNeighbors = getSourceNeighbors(transition);
		
//		. no incoming edges to the transition ??? if T --> P 
		/*if (sourceNeighbors.size() == 0) { 
			return false;
		}*/

		for (int i = 0; i < sourceNeighbors.size(); i++) {
			
			CyEdge arc = getCyEdge(sourceNeighbors.get(i), transition);
			
			condition = condition && (PetriNetUtil.getWeight(cyNetwork, arc) <= PetriNetUtil.getToken(cyNetwork, sourceNeighbors.get(i)));
		}
		
		return condition;
	}

 }

