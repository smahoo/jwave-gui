package de.smahoo.jwave.gui;

import javax.swing.JPanel;

import java.awt.CardLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.smahoo.jwave.cmd.JWaveCommandClass;
import de.smahoo.jwave.cmd.JWaveNodeCommand;
import de.smahoo.jwave.node.JWaveNode;

@SuppressWarnings("serial")
public class PanelNodeDetails extends JPanel{

	
	protected JWaveNode currentNode = null;
	
	public PanelNodeDetails(){
		
		initGui();
		update();
	}
	
	public void setNode(JWaveNode node){
		currentNode = node;
		update();
	}
	
	private void update(){
		String txt = "";
		if (currentNode == null){
			txtDetails.setText(txt);
			return;
		}
		
		txt += "\r\n\r\n\r\nManufacture Details";
		
		txt += "\r\n    manufacturer ID : "+currentNode.getManufactureId();
		txt += "\r\n      device tye ID : "+currentNode.getProductTypeId();
		txt += "\r\n          device ID : "+currentNode.getProductId();
		
		txt += "\r\n\r\nCapabilities";
		
		txt +="\r\n Listening :"+currentNode.isListening();
		txt +="\r\n   Routing :"+currentNode.isRouting();
		
		
		txt +="\r\nDevice Type";
		if (currentNode.getGenericDeviceType() != null){
			txt +="\r\n   generic : "+currentNode.getGenericDeviceType().getName();
		} else {
			txt +="\r\n   generic : ---";
		}
		if (currentNode.getSpecificDeviceType() != null){
			txt +="\r\n   specific : "+currentNode.getSpecificDeviceType().getName();
		} else {
			txt +="\r\n   specific : ---";
		}
		if (currentNode.getBasicDeviceType() != null){
			txt +="\r\n      basic : "+currentNode.getBasicDeviceType().getName();
		} else {
			txt +="\r\n      basic : ---";
		}
		
		txt += "\r\n\r\n";
		
		txt += "Command Classes";
		
		for (JWaveCommandClass cmdClass : currentNode.getCommandClasses()){
			if (cmdClass != null){
				txt += "\r\n   "+cmdClass.getName()+"  v"+cmdClass.getVersion();
			} else {
				// FIXME: that shouldn't be happen
				// Something is wrong handling that list. maybe two accesses at one time?
			}
		}
		
		txtDetails.setText(txt);
		
	}
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_17968443561080");
		
		txtDetails = new JTextArea();
		scrollPane.setViewportView(txtDetails);
	}
	
	private JTextArea txtDetails;
	
}
