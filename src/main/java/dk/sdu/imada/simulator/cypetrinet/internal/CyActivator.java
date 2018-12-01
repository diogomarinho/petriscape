package dk.sdu.imada.simulator.cypetrinet.internal;

import java.util.Properties;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetSelectedNetworkViewsListener;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.AddedEdgesListener;
import org.cytoscape.model.events.AddedNodesListener;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.session.CySessionManager;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.TaskManager;
import org.osgi.framework.BundleContext;

import dk.sdu.imada.simulator.cypetrinet.internal.swing.PetriNetApplication;
import dk.sdu.imada.simulator.cypetrinet.internal.visualization.PetriNetVisualStyle;


//	http://code.cytoscape.org/nexus/service/local/repositories/snapshots/content/archetype-catalog.xml

@SuppressWarnings("rawtypes")

public class CyActivator extends AbstractCyActivator {
	@Override
	public void start(BundleContext context) throws Exception {
		
// . properties .
		
		Properties properties = new Properties();
		
// 		. Visual Style . 
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		
		VisualStyleFactory visualStyleFactory = getService(context, VisualStyleFactory.class);
		
		VisualMappingManager visualMappingManager = getService(context, VisualMappingManager.class);
		
		VisualMappingFunctionFactory dVisualMappingFunctionFactory = getService(context, VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
		
		VisualMappingFunctionFactory pVisualMappingFunctionFactory = getService(context, VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
		
		VisualMappingFunctionFactory  cVisualMappingFunctionFactory = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
		
		
//		. PetriNetVsualStyle .
		PetriNetVisualStyle petriNetVisualStyle = new PetriNetVisualStyle(
																			visualStyleFactory, 
																			visualMappingManager, 
																			dVisualMappingFunctionFactory, 
																			pVisualMappingFunctionFactory, 
																			cVisualMappingFunctionFactory);

//		. Application managers and factories . 
		
		CySessionManager cySessionManager = getService(context, CySessionManager.class);
		
		CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
				
		CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
				
		CyNetworkViewManager cyNetworkViewManager = getService(context, CyNetworkViewManager.class);
				
		CyNetworkViewFactory cyNetworkViewFactory = getService(context, CyNetworkViewFactory.class);
		
		CyLayoutAlgorithmManager layoutAlgorithmManager = getService(context, CyLayoutAlgorithmManager.class);
		
		TaskManager taskManager = getService(context, TaskManager.class);

		PetriNetApplication petriNetApplication = new PetriNetApplication(
																cySessionManager,
																cyApplicationManager,
																networkManager, 
																networkFactory,
																cyNetworkViewManager,
																cyNetworkViewFactory,
																layoutAlgorithmManager,
																taskManager,
																petriNetVisualStyle);
// ..... Register services to Cytoscape:
//		registerService(context,createVisualStyleAction,CyAction.class, new Properties());		
		registerService(context, petriNetApplication, NetworkAddedListener.class, properties);
		registerService(context, petriNetApplication, AddedNodesListener.class, properties);
		registerService(context, petriNetApplication, AddedEdgesListener.class, properties);
		registerService(context, petriNetApplication, SessionLoadedListener.class, properties);
		registerService(context, petriNetApplication, SetSelectedNetworkViewsListener.class, properties);
		
//		registerService(context, petriNetVisualStyle, serviceClass, props);
//		registerAllServices(context, petriNetVisualStyle, properties);
		registerAllServices(context, petriNetApplication, properties);
	}
}
