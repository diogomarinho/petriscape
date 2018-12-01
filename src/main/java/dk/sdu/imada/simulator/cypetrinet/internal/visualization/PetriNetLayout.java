package dk.sdu.imada.simulator.cypetrinet.internal.visualization;

import java.util.Random;
import java.util.Set;

import org.cytoscape.model.CyNode;
import org.cytoscape.view.layout.AbstractLayoutAlgorithm;
import org.cytoscape.view.layout.AbstractLayoutTask;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.model.VisualProperty;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.work.Task;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskMonitor;
import org.cytoscape.work.undo.UndoSupport;

public class PetriNetLayout extends AbstractLayoutAlgorithm {

	public PetriNetLayout(UndoSupport undo) {
		super("customLayout","Custom Layout", undo);		
	}

	@Override
	public TaskIterator createTaskIterator(CyNetworkView cyNetworkView, Object context, Set<View<CyNode>> nodesToLayOut, String attrName) {
		
		final PetriNetLayoutContext petriNetLayoutContext = new PetriNetLayoutContext();
		
		Task task = new AbstractLayoutTask(toString(), cyNetworkView, nodesToLayOut, attrName, undoSupport) {
			
			@Override
			protected void doLayout(TaskMonitor arg0) {
				
				double currX = 0.0d;
				
				double currY = 0.0d;
				
				final VisualProperty<Double> xLoc = BasicVisualLexicon.NODE_X_LOCATION;
				
				final VisualProperty<Double> yLoc = BasicVisualLexicon.NODE_Y_LOCATION;
				
				Random randomGenerator = new Random();
				
				for (final View<CyNode> nView : nodesToLayOut) {
					
					currX = nView.getVisualProperty(xLoc) + petriNetLayoutContext.XRange * randomGenerator.nextDouble();
					
					currY = nView.getVisualProperty(yLoc) + petriNetLayoutContext.YRange * randomGenerator.nextDouble();
					
					nView.setVisualProperty(xLoc,currX);
					
					nView.setVisualProperty(yLoc,currY);
				}
			}
		};
		
		return new TaskIterator(task);
		
	}

}
