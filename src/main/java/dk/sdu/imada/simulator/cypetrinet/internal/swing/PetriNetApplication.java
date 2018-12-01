package dk.sdu.imada.simulator.cypetrinet.internal.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collections;
import java.util.NoSuchElementException;

import javax.swing.JOptionPane;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.events.SetSelectedNetworkViewsEvent;
import org.cytoscape.application.events.SetSelectedNetworkViewsListener;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.events.AddedEdgesEvent;
import org.cytoscape.model.events.AddedEdgesListener;
import org.cytoscape.model.events.AddedNodesEvent;
import org.cytoscape.model.events.AddedNodesListener;
import org.cytoscape.model.events.NetworkAddedEvent;
import org.cytoscape.model.events.NetworkAddedListener;
import org.cytoscape.model.events.NetworkDestroyedEvent;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.session.CySession;
import org.cytoscape.session.CySessionManager;
import org.cytoscape.session.events.SessionLoadedEvent;
import org.cytoscape.session.events.SessionLoadedListener;
import org.cytoscape.view.layout.CyLayoutAlgorithm;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskManager;
import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBMLDocument;
import org.sbml.jsbml.SBMLReader;
import org.sbml.jsbml.Species;
import org.sbml.jsbml.SpeciesReference;

import dk.sdu.imada.simulator.cypetrinet.internal.algorithm.CyNetworkMapping;
import dk.sdu.imada.simulator.cypetrinet.internal.util.PetriNetUtil;
import dk.sdu.imada.simulator.cypetrinet.internal.visualization.PetriNetVisualStyle;

@SuppressWarnings("serial")
public class PetriNetApplication extends PetriNetMenu implements NetworkAddedListener, AddedNodesListener, 
															AddedEdgesListener, NetworkDestroyedListener, SessionLoadedListener,
															SetSelectedNetworkViewsListener {
	
//	. controler of network . 
	boolean sbml2cyControler = false;

// . Network Mannagers and views
	
	CySessionManager cySessionManager;
	
	CyApplicationManager cyApplicationManager;
	
	CyNetworkManager cyNetworkManager;
	
	CyNetworkFactory factory;
	
	
// . Other objects
//	CyNetworkMapping cyNetworkMapping = null;
	
	PetriNetVisualStyle petriNetVisualStyle;
	
// . Variables for events
	protected int num_simulate_clicks = 0;
//	.
	CyNetworkViewManager cyNetworkViewManager;
//	.
	CyNetworkViewFactory cyNetworkViewFactory;
//	.
	TaskManager taskManager;
	
	CyLayoutAlgorithmManager layoutAlgorithmManager;
	
//	. Constructor ...
	public PetriNetApplication(	CySessionManager cySessionManager,
								CyApplicationManager cyApplicationManager,
								CyNetworkManager cyNetworkManager,
								CyNetworkFactory factory,
								CyNetworkViewManager cyNetworkViewManager,
								CyNetworkViewFactory cyNetworkViewFactory,
								CyLayoutAlgorithmManager layoutAlgorithmManager,
								TaskManager taskManager,
								PetriNetVisualStyle petriNetVisualStyle) 
	{
		super();
		
		createEventListeners();
		
		this.cySessionManager = cySessionManager;
		
		this.cyApplicationManager = cyApplicationManager;
		
		this.cyNetworkManager = cyNetworkManager;
		
		this.factory = factory;
		
		this.cyNetworkViewManager = cyNetworkViewManager;
		
		this.cyNetworkViewFactory = cyNetworkViewFactory;
		
		this.layoutAlgorithmManager = layoutAlgorithmManager;
		
		this.taskManager = taskManager;
		
		this.petriNetVisualStyle = petriNetVisualStyle; 
	}

//	. Buttons actions .
	private void createEventListeners() {
		
		genNetworkButton.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) { browserFiles(evt);}});
		
		simulateButton.addActionListener(new ActionListener() {  public void actionPerformed(ActionEvent evt) { simulate(evt);}});
		
		jFileChooser.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) {fchooserSelectedFile(evt);}});
		
       stepsField.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) { getNumberSteps(evt);}});
       
       sbmlField.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent evt) {tfieldChooserFile(evt);}});
	}

// . Generating a defined network 
	private void browserFiles(ActionEvent evt) {
    	jFileChooser.showDialog(new PetriNetMenu(), "ok");
	}
	
//  . selection of the SBML file ... 
	private void fchooserSelectedFile(ActionEvent evt) {
		
		if (evt.getActionCommand().equals(jFileChooser.APPROVE_SELECTION)) {
			
			if (jFileChooser.isAcceptAllFileFilterUsed()) {
				
				try {
					
					sbmlField.setText(jFileChooser.getSelectedFile().getAbsolutePath());
					
					setupSBMLNetwork(sbmlField.getText());
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else {
			sbmlField.setText(" ");
		}
	}

//	. press enter on the text field .
	private void tfieldChooserFile(ActionEvent evt) {
		
		if (!sbmlField.getText().isEmpty()) {
			
			setupSBMLNetwork(sbmlField.getText());
			
		}
	}

//	. load the a SBML Network and converts into a cyNetwork .
	private void setupSBMLNetwork(String path) {
		
		sbml2cyControler = true; //  . this flag is to avoid problems in the addNetworkListener ... 
		
		CyNetwork cyNetwork = this.factory.createNetwork();
		
		PetriNetUtil.createCyPetriNetAttributes(cyNetwork);
		
		boolean value = sbml2Pnet(path, cyNetwork);
			
		if (value) {
			
			this.cyNetworkManager.addNetwork(cyNetwork);
			
			CyNetworkView cyNetworkView = this.cyNetworkViewFactory.createNetworkView(cyNetwork);
			
			this.cyNetworkViewManager.addNetworkView(cyNetworkView);
			
			
			if (cyNetwork.getNodeCount() > 0) {
				
				this.petriNetVisualStyle.colorizeNodes(cyNetwork, cyNetworkView);
				
				updateLegend(cyNetwork);
			}
			
			CyLayoutAlgorithm cyLayoutAlgorithm = layoutAlgorithmManager.getDefaultLayout();
			
			TaskIterator taskIterator = cyLayoutAlgorithm.createTaskIterator(cyNetworkView, cyLayoutAlgorithm.getDefaultLayoutContext(),
					CyLayoutAlgorithm.ALL_NODE_VIEWS, null);
			
			taskManager.execute(taskIterator);
			
			sbml2cyControler = false;
			
		}else {
			
			cyNetwork = null;
		}
		
	}
	
//	. Generate a cyNetwork reading a SBMLFile .
	private boolean sbml2Pnet(String filepath, CyNetwork cyNetwork) {
		
		SBMLReader reader = new SBMLReader();
		
		try {
			
			SBMLDocument sbmlDocument = reader.readSBML(new File(filepath));
			
			Model model = sbmlDocument.getModel();
			
			cyNetwork.getRow(cyNetwork).set(CyNetwork.NAME, model.getName());

			for (int i = 0; i < model.getNumSpecies(); i++) {
				
				Species s = model.getSpecies(i);
				
				CyNode cyPlace = cyNetwork.addNode();
				
				PetriNetUtil.setDefaultValues(cyNetwork, cyPlace);

				PetriNetUtil.setNodeName(cyNetwork, cyPlace, s.getId());
				
				PetriNetUtil.setNodeType(cyNetwork, cyPlace, "place");
				
				PetriNetUtil.setNodeDescription(cyNetwork, cyPlace, s.getName());
				
				PetriNetUtil.updateToken(cyNetwork, (int) s.getInitialAmount(), cyPlace);
			}
			
			int priority = 1;
			
			for (int i = 0; i < model.getNumReactions(); i++) {
				
				Reaction r = model.getReaction(i);
				
				CyNode cyTransition = cyNetwork.addNode();
				
				PetriNetUtil.setDefaultValues(cyNetwork, cyTransition);

				PetriNetUtil.setNodeName(cyNetwork, cyTransition, r.getId());
				
				PetriNetUtil.setNodeType(cyNetwork, cyTransition, "transition");
				
				PetriNetUtil.setNodeDescription(cyNetwork, cyTransition, r.getName());

				PetriNetUtil.setPriority(cyNetwork, cyTransition, model.getNumReactions());
				
				priority++;
				
				generateArcs(cyNetwork, r.getListOfReactants(), cyTransition, r.getListOfProducts());
				
				
				if (r.isReversible()) {
					
					CyNode rTransition = cyNetwork.addNode();
					
					PetriNetUtil.setDefaultValues(cyNetwork, rTransition);

					PetriNetUtil.setNodeName(cyNetwork, rTransition, r.getId()+"_R");
					
					PetriNetUtil.setNodeType(cyNetwork, rTransition, "transition");
					
					PetriNetUtil.setNodeDescription(cyNetwork, rTransition, r.getName()+"_R");

					PetriNetUtil.setPriority(cyNetwork, rTransition, model.getNumReactions());
					
					generateArcs(cyNetwork, r.getListOfProducts(), rTransition, r.getListOfReactants());
				}
			}
			
			PetriNetUtil.setPetriNet(cyNetwork, true);
			
			return true;
			
		}catch(Exception exception) {
			
			JOptionPane.showMessageDialog(null, "Error in parsing the SBML file !");
			
			exception.printStackTrace();
			
			return false;
		}
	}
	
//	. just create the cyEdges .
	private void generateArcs(CyNetwork cyNetwork, ListOf<SpeciesReference> source, CyNode transition, ListOf<SpeciesReference> target) {
		
		for (SpeciesReference s : source) {
			
			Long sSUID = PetriNetUtil.getSUID(cyNetwork, s.getSpecies());
			
			int weight = (int) s.getStoichiometry();
			
			CyNode cySource = cyNetwork.getNode(sSUID);
			
			CyEdge cyEdge = cyNetwork.addEdge(cySource, transition, true);
			
			PetriNetUtil.setDefaultValues(cyNetwork, cyEdge);
			
			PetriNetUtil.setWeight(cyNetwork, cyEdge, weight);
		}
		
		for (SpeciesReference t : target) {
			
			Long sSUID = PetriNetUtil.getSUID(cyNetwork, t.getSpecies());
			
			int weight = (int) t.getStoichiometry();
			
			CyNode cyTarget = cyNetwork.getNode(sSUID);
			
			CyEdge cyEdge = cyNetwork.addEdge(transition, cyTarget, true);
			
			PetriNetUtil.setDefaultValues(cyNetwork, cyEdge);
			
			PetriNetUtil.setWeight(cyNetwork, cyEdge, weight);
			
		}
	}
	
//	. Set number of steps for the simulation .
	public int globalNumberOfSteps = 0;
	
	private void getNumberSteps(ActionEvent evt) {
		
		globalNumberOfSteps = Integer.parseInt(stepsField.getText());
		
	}
	
//	. simulation of token game step per step .
	private void simulate(ActionEvent evt) {
			
		CyNetwork toSimulateNetwork = this.cyApplicationManager.getCurrentNetwork();
		
		if (toSimulateNetwork == null) {
			
			JOptionPane.showMessageDialog(null, "No network was selected.");
			
		}else {
			
			if (checkNetworkConsistency(toSimulateNetwork)) {
				
				CyNetworkView toUpdateView = this.cyApplicationManager.getCurrentNetworkView();
				
				CyNetworkMapping cyNetworkMapping = new CyNetworkMapping(toSimulateNetwork);

				getNumberSteps(evt);
				
				PetriNetUtil.resetTransitionActivity(toSimulateNetwork);
				
				for (int i = 0; i < globalNumberOfSteps; i++) {
					
					cyNetworkMapping.updateMarking();
					
					System.out.println("....");
				}
				
				updateLegend(toSimulateNetwork);
				
				if (toUpdateView == null) {
					
					JOptionPane.showMessageDialog(null, "No network view to update.");
					
				}else {
					
					this.petriNetVisualStyle.colorizeNodes(toSimulateNetwork, this.cyNetworkViewManager.getNetworkViews(toSimulateNetwork).iterator().next());

				}
			}
		}
	}
		
//	. Update the color gradient on the tab menu . 
	private void updateLegend(CyNetwork toUpdateNetwork) {
		
		Integer maxT = Collections.max(PetriNetUtil.getTransitionActivityList(toUpdateNetwork));
		
		Integer maxP = Collections.max(PetriNetUtil.getMarkingVector(toUpdateNetwork)); 
		
		
		this.jLabel7.setText(maxT.toString());
		
		this.jLabel8.setText(maxP.toString());
		
	}
	
//	. handle the events in cytoscape: Add a CyNetwork . 
	@Override
	public void handleEvent(NetworkAddedEvent e) {
		
		if (!sbml2cyControler) {
			
			CyNetwork cyNetwork = e.getNetwork();
			
			if (!PetriNetUtil.hasAttributes(cyNetwork)) {
				
				PetriNetUtil.createCyPetriNetAttributes(cyNetwork);
				
				for (CyNode  cyNode : cyNetwork.getNodeList()) {
					PetriNetUtil.setDefaultValues(cyNetwork, cyNode);
				}
				
				for (CyEdge cyEdge : cyNetwork.getEdgeList()) {
					PetriNetUtil.setDefaultValues(cyNetwork, cyEdge);
				}		
				
				PetriNetUtil.setPetriNet(cyNetwork, true);
			}
		}
	}
	
//	. handle the events in cytoscape: add a cyNode .
	@Override
	public void handleEvent(AddedNodesEvent e) {
		
		CyNode last = e.getSource().getNodeList().iterator().next();
		
		PetriNetUtil.setDefaultValues(e.getSource(), last);
	}

//	. handle the events in cytoscape: add a cyEdge . 
	@Override
	public void handleEvent(AddedEdgesEvent e) {
		
		CyNetwork cyNetwork = e.getSource();

		CyEdge last = e.getSource().getEdgeList().iterator().next();
		
		PetriNetUtil.setDefaultValues(cyNetwork, last);
	}

//	. handle the events in cytoscape: Destroy Network . 
	@Override
	public void handleEvent(NetworkDestroyedEvent e) {
		
//		this.petriNetVisualStyle.setDefaultColorNode();
		
		this.jLabel7.setText("max");
		
		this.jLabel8.setText("max");
		
	}

//	. handle the events in cytoscape: open a session .
	@Override
	public void handleEvent(SessionLoadedEvent sessionEvent) {
		
//		this.petriNetVisualStyle.generateVisualStyle();
		
		CySession cySession = sessionEvent.getLoadedSession();
		
		boolean check = false;
		
		for (VisualStyle vs : cySession.getVisualStyles()) {
			
			if (vs.getTitle().equalsIgnoreCase("petriNetVS")) {
				
				check = true;
				
				this.petriNetVisualStyle.updateVisualStyle(vs);
				
				break;
			}
		}
		
		if (!check) {
			
			this.petriNetVisualStyle.generateVisualStyle();
		}
		
		for (CyNetworkView cyNetworkView : cySession.getNetworkViews()) {
			
			CyNetwork cyNetwork = cyNetworkView.getModel();
			
			if (cyNetwork.getNodeCount() > 0) {
				
				this.petriNetVisualStyle.colorizeNodes(cyNetwork, cyNetworkView);
				
				updateLegend(cyNetwork);

				
			}else {
				
				this.petriNetVisualStyle.setDefaultColorNode();
				
				this.jLabel7.setText("max");
				
				this.jLabel8.setText("max");
			}
			
		}
	}

//	. Selection netowrk view method for update the color of nodes . 
	@Override
	public void handleEvent(SetSelectedNetworkViewsEvent arg0) {
		
		CyNetworkView cyNetworkView = arg0.getNetworkViews().iterator().next();
		
		CyNetwork cyNetwork = cyNetworkView.getModel();
		
		if (cyNetwork.getNodeCount() > 0) {
			
			petriNetVisualStyle.colorizeNodes(cyNetwork, cyNetworkView);
			
			updateLegend(cyNetwork);
			
			cyNetworkView.updateView();
			
		}else {
			
			petriNetVisualStyle.setDefaultColorNode();
			
			this.jLabel7.setText("max");
			
			this.jLabel8.setText("max");
			
			cyNetworkView.updateView();
		}
	}
	
	private boolean checkNetworkConsistency(CyNetwork cyNetwork) {
		
		CyTable nodeTable = cyNetwork.getDefaultNodeTable();
		
		CyTable arcTable = cyNetwork.getDefaultEdgeTable();
		
		for (CyRow  row : nodeTable.getAllRows()) {
			
			if (row.get("token", Integer.class) < 0){
				JOptionPane.showMessageDialog(null, "Network error: negative token value");
				return false;
			}
			
			if (row.get("priority", Integer.class) < 0){
				JOptionPane.showMessageDialog(null, "Network error: negative priority value");
				return false;
			}
			
			if (row.get("tactivity", Integer.class) < 0){
				JOptionPane.showMessageDialog(null, "Network error: negative tactivity value");
				return false;
			}
			
			if (!(row.get("type", String.class).equals("place") || row.get("type", String.class).equals("transition"))){
				JOptionPane.showMessageDialog(null, "Network error: " + row.get("type", String.class) + " is a invalid node type");
				return false;
			}
		}
		
		for (CyRow row : arcTable.getAllRows()) {
			
			if (row.get("weight", Integer.class) < 0) {
				
				JOptionPane.showMessageDialog(null, "Network error: negative arc weight value");
				
				return false;
			}
		}
		
		for (CyEdge cyEdge : cyNetwork.getEdgeList()) {
			
			CyNode source = cyEdge.getSource();
			
			CyNode target = cyEdge.getTarget();
			
			if (PetriNetUtil.getCyNodeType(cyNetwork, source).equals(PetriNetUtil.getCyNodeType(cyNetwork, target))) {
				
				JOptionPane.showMessageDialog(null, "Network error: nodes " + PetriNetUtil.getNodeName(cyNetwork, source) + " and " + 
						PetriNetUtil.getNodeName(cyNetwork, target) + " can't be connected.");

				return false;
			}
			
		}
		return true;
	}
}
