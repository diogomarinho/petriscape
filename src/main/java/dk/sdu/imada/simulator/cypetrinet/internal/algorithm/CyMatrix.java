package dk.sdu.imada.simulator.cypetrinet.internal.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
//import org.la4j.matrix.dense.Basic2DMatrix;

import dk.sdu.imada.simulator.cypetrinet.internal.util.CyNodePriorityComparator;
import dk.sdu.imada.simulator.cypetrinet.internal.util.PetriNetUtil;

public class CyMatrix {
	
	CyNetwork cyNetwork;
	
	public CyMatrix(CyNetwork cyNetwork) {
		
		this.cyNetwork = cyNetwork;
		
	}
	
	public void setCyNetwork(CyNetwork cyNetwork) {
		
		this.cyNetwork = cyNetwork;
		
	}
	
//	. Get transitions ordered by priority min to max .
	protected ArrayList<CyNode> getOrderedTransitions() {
		
		ArrayList<CyNode> transitions = PetriNetUtil.getTransitions(cyNetwork);

		CyNodePriorityComparator cyNodePriorityComparator = new CyNodePriorityComparator(cyNetwork);
		
		Collections.sort(transitions, cyNodePriorityComparator);

		
		return transitions;
	}
	
//	. return a transitions in order to fire ... 
	protected ArrayList<CyNode> getFiringOrder() {
		
		ArrayList<CyNode> transitions = getOrderedTransitions();
		
		HashSet<Integer> priorityValues = priorityList();
		
		ArrayList<CyNode> tFireSequence = new ArrayList<CyNode>();
		
		for (Integer priority : priorityValues) {
			
			ArrayList<CyNode> tlist = getTransitionsByPriority(priority.intValue(), transitions);
			
//			. arbitrary order for transitions with the same priority = same likelihood.
			Collections.shuffle(tlist); // . change that for a user probability choice ... 
				
			for (CyNode t : tlist) {
				tFireSequence.add(t);
			}
		}
		
		return tFireSequence;
	}
	
//	. Get transitions with the same priority number . 
	private ArrayList<CyNode> getTransitionsByPriority(int value, ArrayList<CyNode> transitions) {
		
		ArrayList<CyNode> tlist = new ArrayList<CyNode>();
		
		for  (CyNode t : transitions) {
			if (PetriNetUtil.getPriority(cyNetwork, t) == value) {
				tlist.add(t);
			}
		}
		
		return tlist;
	}
	
//	Non redundant priority integer list 
	private  HashSet<Integer> priorityList() {
		
		ArrayList<Integer> p = new ArrayList<Integer>();
		
		ArrayList<CyNode> transitions = PetriNetUtil.getTransitions(cyNetwork);
		
		for (CyNode t : transitions) {
			p.add(PetriNetUtil.getPriority(cyNetwork, t));
		}
		
		Collections.sort(p);

		HashSet<Integer> hashSet =  new HashSet<Integer>(p);

		
		return hashSet;
	}
	
//	. get arrayList of all cyEdges in the cyNetwork .
	protected ArrayList<CyEdge> getArcs() {
		
		ArrayList<CyEdge> arcs = new ArrayList<CyEdge>();
		
		for (CyEdge cyEdge : this.cyNetwork.getEdgeList()) {
			arcs.add(cyEdge);
		}
		
		return arcs;
	}
	
//	. get the cyEdge between the cynodes a and b .
	protected CyEdge getCyEdge(CyNode a, CyNode b) {
		
		for (CyEdge cyEdge : this.cyNetwork.getEdgeList()) {
			
			CyNode source = cyEdge.getSource();
			
			CyNode target = cyEdge.getTarget();
			
			if ((source.equals(a) && target.equals(b))) 
				return cyEdge;
		}
		
		return null;
	}
	
//	. check if the cynodes place and transition form a directed Arc .
	protected boolean isDirectedArc(CyNode place, CyNode transition) {
		
		for (CyEdge cyEdge : this.cyNetwork.getEdgeList()) {
			
			CyNode source = cyEdge.getSource();
			
			CyNode target = cyEdge.getTarget();
			
			if ((source.equals(place) && target.equals(transition))) 
				return true;
		}
		
		return false;
	}

//	. vector with the number of tokens in each place . 	
	public double[] getMarkingVector() {
		
		ArrayList<CyNode> places = PetriNetUtil.getPlaces(cyNetwork);
		
		double matrix[] = new double [places.size()];

		for (int i = 0; i <  places.size(); i++) {
			
			matrix[i] = PetriNetUtil.getToken(cyNetwork, places.get(i));
		}
		
		return matrix;
	}
	
//	. vector with the number of tokens in each place . 
	public double[][] getMarkingMatrix() {
		
		ArrayList<CyNode> places = PetriNetUtil.getPlaces(cyNetwork);
		
		double matrix[][] = new double [places.size()][1];

		for (int i = 0; i <  places.size(); i++) {
			
			int token = PetriNetUtil.getToken(cyNetwork, places.get(i));
			
			matrix[i][0] = token;
		}
		
		return matrix;
	}
	
//	. matrix for token transference .
	public double [][] getFlowRuleMatrix() {
		
		ArrayList<CyNode> places = PetriNetUtil.getPlaces(cyNetwork);
		
		ArrayList<CyNode> transitions = getOrderedTransitions();
		
		double matrix[][] = new double[places.size()][transitions.size()];
		
		Iterator<CyEdge> iterator = cyNetwork.getEdgeList().iterator();
		
		while (iterator.hasNext()) {
			
			CyEdge arc = iterator.next();
			
			CyNode source = arc.getSource();
			
			CyNode target = arc.getTarget();
			
			int indexPlace = 0;
			
			int indexTransition = 0;
			
			if (PetriNetUtil.isPlace(cyNetwork, source)) {
				
				indexPlace = places.indexOf(source);
				
				indexTransition = transitions.indexOf(target);
				
				matrix[indexPlace][indexTransition] = -1 * PetriNetUtil.getWeight(cyNetwork, arc);
				
			}else {
				
				indexTransition = transitions.indexOf(source);
				
				indexPlace = places.indexOf(target);
				
				matrix[indexPlace][indexTransition] = PetriNetUtil.getWeight(cyNetwork, arc);
			}
		}
		
		return matrix;
	}
	
//	. get the list of neighbors of a specific node 
	protected ArrayList<CyNode> getSourceNeighbors(CyNode referenceNode) {
		
		ArrayList<CyNode> neighbors = new  ArrayList<CyNode>();

		ArrayList<CyEdge> arcs = getArcs();
		
		for (CyEdge cyEdge : arcs) {
			
			CyNode source = cyEdge.getSource();
			
			CyNode target = cyEdge.getTarget();
			
			if (target.equals(referenceNode)) {
				
				neighbors.add(source);
			}
		}
		
		return neighbors;
	}
	
//	. Get all incoming arcs from the reference node 
	protected ArrayList<CyEdge> getIncomingArcs(CyNode referenceNode) {
		
		ArrayList<CyEdge> incomingArcs = new  ArrayList<CyEdge>();

		ArrayList<CyEdge> arcs = getArcs();
		
		for (CyEdge cyEdge : arcs) {
			
			CyNode target = cyEdge.getTarget();
			
			if (target.equals(referenceNode)) {
				
				incomingArcs.add(cyEdge);
			}
		}
		
		return incomingArcs;
	}	
	
//	. Get the list of neighbors of a specific node 
	protected ArrayList<CyNode> getTargetNeighbors(CyNode referenceNode) {
		
		ArrayList<CyNode> neighbors = new  ArrayList<CyNode>();

		ArrayList<CyEdge> arcs = getArcs();
		
		for (CyEdge cyEdge : arcs) {
			
			CyNode source = cyEdge.getSource();
			
			CyNode target = cyEdge.getTarget();
			
			if (source.equals(referenceNode)) {
				
				neighbors.add(target);
			}
		}
		
		return neighbors;
	}
	
//	. Get all outgoing arcs from the reference node 
	protected ArrayList<CyEdge> getOutgoingArcs(CyNode referenceNode) {
		
		ArrayList<CyEdge> outgoingArcs = new  ArrayList<CyEdge>();

		ArrayList<CyEdge> arcs = getArcs();
		
		for (CyEdge cyEdge : arcs) {

			CyNode source = cyEdge.getSource();
			
			if (source.equals(referenceNode)) {
				
				outgoingArcs.add(cyEdge);
			}
		}
		
		return outgoingArcs;
	}

//	. log .
	/*public void logInitilMarking() {
		
		System.out.println("Intial Marking ...");
		
		System.out.println(new Basic2DMatrix(getFlowRuleMatrix()));
	}*/
}
