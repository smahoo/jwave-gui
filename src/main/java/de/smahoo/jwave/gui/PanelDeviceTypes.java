package de.smahoo.jwave.gui;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.CardLayout;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import de.smahoo.jwave.cmd.JWaveGenericDeviceType;
import de.smahoo.jwave.cmd.JWaveSpecificDeviceType;

@SuppressWarnings("serial")
public class PanelDeviceTypes extends JPanel{
	
	private List<JWaveGenericDeviceType> devTypes = null;
	
	public PanelDeviceTypes(){
		
		initGui();
		update();
	}
	
	
	public void setDeviceTypes(List<JWaveGenericDeviceType> devTypes){
		this.devTypes = devTypes;
		update();
	}
	
	private void update(){
		buildTree();
	}
	
	private void buildTree(){
		 DefaultMutableTreeNode root = new DefaultMutableTreeNode("Z-Wave Generic Device Types");
		 DefaultMutableTreeNode genDevNode;
		 DefaultMutableTreeNode specDevNode;
		 
		 for (JWaveGenericDeviceType genType : devTypes){
			 if (genType != null){
				 genDevNode = new DefaultMutableTreeNode(genType);
				 
				 for (JWaveSpecificDeviceType specType : genType.getSpecificDeviceTypes()){
					 specDevNode = new DefaultMutableTreeNode(specType);
					 genDevNode.add(specDevNode);
				 }
				 
				 
				 root.add(genDevNode);
			 }
		 }
		 
		 tree.setModel(new DefaultTreeModel(root));
	}
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_5344553769015");
		
		tree = new JTree();
		scrollPane.setViewportView(tree);
	}
	
	
	private JTree tree;
}
