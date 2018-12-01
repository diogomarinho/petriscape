package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import java.util.HashMap;
import java.util.Map;

import org.cytoscape.task.NetworkViewTaskFactory;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TunableSetter;

public class PetriNetLayoutFactory implements NetworkViewTaskFactory {
	
	CyLayoutAlgorithmManager cyLayoutAlgorithmManager;
	
	TunableSetter tunableSetter;
	
	
	public PetriNetLayoutFactory(CyLayoutAlgorithmManager cyLayoutAlgorithmManager, TunableSetter tunableSetter) {
		
		this.cyLayoutAlgorithmManager = cyLayoutAlgorithmManager;
		
		this.tunableSetter = tunableSetter;
	}

	@Override
	public TaskIterator createTaskIterator(CyNetworkView cyNetworkView) {
		
		CyLayoutAlgorithm cyLayoutAlgorithm = this.cyLayoutAlgorithmManager.getLayout("CustomLayout");
		
		Object context = cyLayoutAlgorithm.createLayoutContext();
		
		Map<String, Object> tunableValues = new HashMap<String, Object>();
		
		tunableValues.put("XRange", 50);
		
		tunableValues.put("yRange", 50);
		
		String layoutAttribute = null;
		
		return cyLayoutAlgorithm.createTaskIterator(cyNetworkView, context, CyLayoutAlgorithm.ALL_NODE_VIEWS, layoutAttribute);
	}

	@Override
	public boolean isReady(CyNetworkView cyNetworkView) {
		
		return cyNetworkView != null;
	}

}
