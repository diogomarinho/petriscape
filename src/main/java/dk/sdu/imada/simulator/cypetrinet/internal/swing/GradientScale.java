package dk.sdu.imada.simulator.cypetrinet.internal.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GradientScale extends JPanel {
	
	   private void doDrawing(Graphics g) {
	        
	        Graphics2D g2d = (Graphics2D) g;

	        GradientPaint gp1 = new GradientPaint(10, 10, 
	            Color.white, 150, 20, Color.green, false);
	        
	        g2d.setPaint(gp1);
	        g2d.fillRect(10, 25, 145, 20);
	      
	        GradientPaint gp2 = new GradientPaint(10, 20, 
	            Color.white, 150, 20, Color.red, true);

	        g2d.setPaint(gp2);
	        g2d.fillRect(10, 90, 145, 20);
	        
	    } 

	    @Override
	    public void paintComponent(Graphics g) {
	        
	    	super.paintComponent(g);
	        
	        doDrawing(g);

	    }

}
