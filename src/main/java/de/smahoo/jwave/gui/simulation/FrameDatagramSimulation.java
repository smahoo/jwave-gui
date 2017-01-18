package de.smahoo.jwave.gui.simulation;

import javax.swing.JFrame;

import de.smahoo.jwave.JWaveController;

import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class FrameDatagramSimulation extends JFrame {
	
	protected JWaveController cntrl;
	
	public FrameDatagramSimulation(JWaveController cntrl) {
		this.cntrl = cntrl;
		init();
	}

	
	private void init(){
		PanelDatagramSimulation panelDatagramSimulation = new PanelDatagramSimulation(cntrl);
		getContentPane().add(panelDatagramSimulation, BorderLayout.CENTER);
	}
	
}
