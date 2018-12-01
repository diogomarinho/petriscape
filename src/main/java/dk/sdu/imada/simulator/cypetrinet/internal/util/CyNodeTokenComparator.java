package dk.sdu.imada.simulator.cypetrinet.internal.util;

import java.util.Comparator;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;

public class CyNodeTokenComparator implements Comparator<CyNode> {
	
	CyNetwork cyNetwork;
	
	public CyNodeTokenComparator(CyNetwork  cyNetwork) {
		
		this.cyNetwork = cyNetwork;
		
	}

	public int compare(CyNode a, CyNode b) {
		
		Integer ta = new Integer(PetriNetUtil.getToken(cyNetwork, a));
		
		Integer tb = new Integer(PetriNetUtil.getToken(cyNetwork, b));
		
		return ta.compareTo(tb);
		
	}

}
