package dk.sdu.imada.simulator.cypetrinet.internal.util;

import java.util.ArrayList;
import java.util.Iterator;

import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public class PetriNetUtil {
	
	public static void createCyPetriNetAttributes(CyNetwork cyNetwork) {
		
		cyNetwork.getDefaultNetworkTable().createColumn("PN", Boolean.class, true, false);

		cyNetwork.getDefaultNodeTable().createColumn("type", String.class, false);
		
		cyNetwork.getDefaultNodeTable().createColumn("description", String.class, false);
		
		cyNetwork.getDefaultNodeTable().createColumn("token", Integer.class, false);
		
		cyNetwork.getDefaultNodeTable().createColumn("priority", Integer.class, true);
		
		cyNetwork.getDefaultEdgeTable().createColumn("weight", Integer.class, false);
		
		cyNetwork.getDefaultEdgeTable().createColumn("description", String.class, false);
		
		cyNetwork.getDefaultNodeTable().createColumn("tactivity", Integer.class, false);
		
		cyNetwork.getDefaultNodeTable().createColumn("label", String.class, false);
	}
	
	public static boolean hasAttributes(CyNetwork cyNetwork) {
		
		boolean value = false;
		
		if (cyNetwork.getDefaultNetworkTable().getColumn("PN")!=null) {
			
			value = true;
		}
		
		return value;
	}
	
	public static void setPetriNet(CyNetwork cyNetwork, boolean value) {
		cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID()).set("PN", value);
	}
	
	public static boolean isValidPetriNet(CyNetwork cyNetwork) {
		return cyNetwork.getDefaultNetworkTable().getRow(cyNetwork.getSUID()).get("PN", Boolean.class);
	}
	
	public static void setNodeName(CyNetwork cyNetwork, CyNode cyNode, String value) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("name", value);
	}
	
	public static void setNodeDescription(CyNetwork cyNetwork, CyNode cyNode, String value) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("description", value);
	}

	public static void setEdgeName(CyNetwork cyNetwork, CyEdge cyEdge, String value) {
		cyNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("name", value);
	}
//	. get cyNode name . 
	public static String getNodeName(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("name", String.class);
	}
	
//	. get cyEdge name
	public static String getEdgeName(CyNetwork cyNetwork, CyEdge cyEdge) {
		return cyNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).get("name", String.class);
	}

//	. get the number of tokens in a cyNode 
	public static int getToken(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("token", Integer.class);
	}

//	. Update the token value .
	public static void updateToken(CyNetwork cyNetwork, int value, CyNode cyNode) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("token", value);
	}

//	. check if the cyNode type is null .
	public static boolean isTypeNull(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("type", String.class) == null;
	}
	
//	. check if the cyNode is a place .
	public static boolean isPlace(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("type", String.class).equals("place");
	}
	
//	. check if the cynode is a transition .
	public static boolean isTransition(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("type", String.class).equals("transition");
	}
	
//	. set cyNode type .	
 	public static void setNodeType(CyNetwork cyNetwork, CyNode cyNode, String type) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("type", type);
	}
 	
//	. get the cyEdge weight .
	public static int getWeight(CyNetwork cyNetwork, CyEdge arc) {
		return cyNetwork.getDefaultEdgeTable().getRow(arc.getSUID()).get("weight", Integer.class);
	}
	
//	. set the cyEdge weight .
	public static void setWeight(CyNetwork cyNetwork, CyEdge arc, int weight) {
		
		cyNetwork.getDefaultEdgeTable().getRow(arc.getSUID()).set("weight", weight);
		
	}
	
//	. get the cyNode (for transitions only ...) priority .
	public static int getPriority(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("priority", Integer.class);
	}
	
//	. set cynode (for transitions) priority . 
	public static void setPriority(CyNetwork cyNetwork, CyNode cyNode, int value) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("priority", value);
	}
	
//	. Update transition tactive 
	public static void updateTransitionActivity(CyNetwork cyNetwork, int value, CyNode cyNode) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("tactivity", value);
	}
	
//	. get transition tactive 
	public static int getTransitionActivity(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("tactivity", Integer.class);
	}

//	. return fitness of the cyNode
	public static double getFit(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("fit", Double.class);
	}
	
	public static String getCyNodeType(CyNetwork cyNetwork, CyNode cyNode) {
		return cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).get("type", String.class);
	}
	
	public static void setDefaultValues(CyNetwork cyNetwork, CyEdge cyEdge) {
		cyNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("weight", 1);
		cyNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("name", "");
		cyNetwork.getDefaultEdgeTable().getRow(cyEdge.getSUID()).set("description", "");
	}	
	
	public static void setDefaultValues(CyNetwork cyNetwork, CyNode cyNode) {
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("name", "");
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("description", "");
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("token", 0);
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("type", "place");
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("priority", 0);
		cyNetwork.getDefaultNodeTable().getRow(cyNode.getSUID()).set("tactivity", 0);
	}
	
	public static long getSUID(CyNetwork cyNetwork, String name) {
		
		for (CyNode cyNode : cyNetwork.getNodeList()) {
			
			if (name.equals(PetriNetUtil.getNodeName(cyNetwork, cyNode))) {
				return cyNode.getSUID();
			}
		}
		
		return -1;
	}

//	. get totalNumber of places 
	public static int getTotalPlaces(CyNetwork cyNetwork) {
		
		int total = 0;
		
		Iterator<CyNode> iterator = cyNetwork.getNodeList().iterator();
		
		while (iterator.hasNext()) {
			
			CyNode cyNode = iterator.next();
					
			if (PetriNetUtil.isPlace(cyNetwork, cyNode)) {
				total++;
			}
		}
		
		return total;
	}
	
//	. get totalNumber of places .
	public static int getTotalTransitions(CyNetwork cyNetwork) {
		
		int total = 0;
		
		Iterator<CyNode> iterator = cyNetwork.getNodeList().iterator();
		
		while (iterator.hasNext()) {
			
			CyNode cyNode = iterator.next();
					
			if (PetriNetUtil.isTransition(cyNetwork, cyNode)) {
				total++;
			}
		}
		return total;
	}
	
//	. get total tokens in the network . 	
	public static int getTotalTokens(CyNetwork cyNetwork) {
		
		int total = 0;
		
		Iterator<CyNode> iterator = cyNetwork.getNodeList().iterator();
		
		while (iterator.hasNext()) {
			
			CyNode cyNode = iterator.next();
					
			if (PetriNetUtil.isPlace(cyNetwork, cyNode)) {
				total+= PetriNetUtil.getToken(cyNetwork, cyNode);
			}
		}
		
		return total;
	}
	
//	. get the total sum of fired Transitions
	public static int getTotalFiredTransitions(CyNetwork cyNetwork) {
		
		int total = 0;
		
		Iterator<CyNode> iterator = cyNetwork.getNodeList().iterator();
		
		while (iterator.hasNext()) {
			
			CyNode cyNode = iterator.next();
					
			if (PetriNetUtil.isPlace(cyNetwork, cyNode)) {
				
				total+= PetriNetUtil.getTransitionActivity(cyNetwork, cyNode);
				
			}
		}
		return total;
	}
	
//	. return an arraylist with the tactivity of each transition .
	public static ArrayList<Integer> getTransitionActivityList(CyNetwork cyNetwork) {
		
		ArrayList<Integer> firedTransitionVec = new ArrayList<Integer>();
		
		for (CyNode transition : getTransitions(cyNetwork)) {
			
			firedTransitionVec.add(getTransitionActivity(cyNetwork, transition));
			
		}
		
		return firedTransitionVec;
	}
	
//	. get marking Integer vector ... 
	public static ArrayList<Integer> getMarkingVector(CyNetwork cyNetwork) {
		
		ArrayList<Integer> marking = new ArrayList<Integer>();

		for (CyNode cyNode : cyNetwork.getNodeList()) {
			
			if (isPlace(cyNetwork, cyNode)) {
				marking.add(getToken(cyNetwork, cyNode));
			}
		}
		
		return marking;
	}
	
//	. get place array list .
	public static ArrayList<CyNode> getPlaces(CyNetwork cyNetwork) {
		
		ArrayList<CyNode> places = new ArrayList<CyNode>();
		
		for (CyNode cyNode : cyNetwork.getNodeList()) {
			
			if (isPlace(cyNetwork, cyNode)) {
				
				places.add(cyNode);
			}
		}
		
		return places;
	}
	
//	. get transitions array list .
	public static ArrayList<CyNode> getTransitions(CyNetwork cyNetwork) {
		
		ArrayList<CyNode> transitions = new ArrayList<CyNode>();
		
		for (CyNode cyNode : cyNetwork.getNodeList()) {
			
			if (isTransition(cyNetwork, cyNode)) {
				
				transitions.add(cyNode);
			}
		}
		return transitions;
	}

//	. Reset to zero all transition call ... 
	public static void resetTransitionActivity(CyNetwork cyNetwork) {
		
		for (CyNode transition : getTransitions(cyNetwork)) {
			updateTransitionActivity(cyNetwork, 0, transition);
		}
	}
	
}
