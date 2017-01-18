package de.smahoo.jwave.gui;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.Dimension;

import javax.swing.JButton;

import de.smahoo.jwave.cmd.JWaveCommand;
import de.smahoo.jwave.cmd.JWaveCommandClass;
import de.smahoo.jwave.cmd.JWaveCommandClassSpecification;
import de.smahoo.jwave.node.JWaveNode;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FrameCommandClasses extends JFrame {

	private JWaveCommandClassSpecification zwaveDef = null;
	private SelectionListener selectionListener;
	private JWaveCommandClass currCmdClass = null;
	
	public FrameCommandClasses(){		
		selectionListener = new SelectionListener() {
			
			@Override
			public void onCommandSelected(JWaveCommand cmd) {
				evaluateCommandSelection(cmd);			
			}
			
			@Override
			public void onCommandClassSelected(JWaveCommandClass cmdClass) {
				evaluateCommandClassSelection(cmdClass);				
			}
			
			@Override
			public void onNodeSelected(JWaveNode node){
				//
			}
		};
		
		initGui();
		update();
	}
	
	public void setDefinitions(JWaveCommandClassSpecification zWaveDef){
		this.zwaveDef = zWaveDef;
		update();
	}
	
	private void evaluateCommandClassSelection(JWaveCommandClass cClass){
		currCmdClass = cClass;
		panelZWaveCommands.setCommandList(cClass.getCommandList());		
	}
	
	private void evaluateCommandSelection(JWaveCommand cmd){
		panelZWaveParamDetails.setParam(currCmdClass, cmd);
	}
	
	private void update(){
		if (zwaveDef == null){
			return;
		}
		if (panelCommandClassList == null){
			return;
		}
		panelCommandClassList.setCommandList(zwaveDef.getCommandClasses());
	}
	
	private void onCloseClicked(){
		this.dispose();
	}
	
	private void initGui(){
		setTitle("ZWave Command Classes");
		setSize(500,300);
		JSplitPane splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		panelCommandClassList = new PanelCommandClassList();
		panelCommandClassList.addSelectionListener(selectionListener);
		splitPane.setLeftComponent(panelCommandClassList);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane.setRightComponent(splitPane_1);
		
		panelZWaveCommands = new PanelZWaveCommands();
		panelZWaveCommands.addSelectionListener(selectionListener);
		splitPane_1.setLeftComponent(panelZWaveCommands);
		
		panelZWaveParamDetails = new PanelZWaveParamDetails();
		splitPane_1.setRightComponent(panelZWaveParamDetails);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(50, 35));
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCloseClicked();
			}
		});
		panel.add(btnClose);
	}
	
	
	private PanelCommandClassList panelCommandClassList;
	private PanelZWaveCommands panelZWaveCommands;
	private PanelZWaveParamDetails panelZWaveParamDetails;
}
