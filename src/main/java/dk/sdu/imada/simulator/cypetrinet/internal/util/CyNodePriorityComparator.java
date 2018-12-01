package dk.sdu.imada.simulator.cypetrinet.internal.util;

import java.util.Comparator;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public class CyNodePriorityComparator implements Comparator<CyNode> {
	
	CyNetwork cyNetwork;
	
	public CyNodePriorityComparator(CyNetwork cyNetwork) {
		this.cyNetwork = cyNetwork;
	}
	
	@Override
	public int compare(CyNode a, CyNode b) {

		Integer pa = new Integer(PetriNetUtil.getPriority(cyNetwork, a));
		
		Integer pb = new Integer(PetriNetUtil.getPriority(cyNetwork, b));
		
		return pa.compareTo(pb);
	}
}
