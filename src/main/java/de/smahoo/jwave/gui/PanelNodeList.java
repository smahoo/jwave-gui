package de.smahoo.jwave.gui;

import java.util.List;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.event.JWaveEvent;
import de.smahoo.jwave.event.JWaveEventListener;
import de.smahoo.jwave.node.JWaveNode;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class PanelNodeList extends JPanel{

	JWaveController cntrl = null;
	SelectionListener selectionListener = null;
	@SuppressWarnings("unused")
	private JWaveEventListener eventListener = null;
	
	public PanelNodeList(){
		
		initGui();
		update();
	}
	
	public void setCntrl(JWaveController cntrl){
		this.cntrl = cntrl;
		cntrl.addCntrlListener(new JWaveEventListener() {
			
			@Override
			public void onJWaveEvent(JWaveEvent event) {
				evaluateEvent(event);
				
			}
		});
		update();
	}
	
	public void setSelectionListener(SelectionListener selectionListener){
		this.selectionListener = selectionListener;
	}
	
	private void evaluateEvent(JWaveEvent event){
		switch (event.getEventType()){
			case NODE_EVENT_NODE_ADDED:
			case NODE_EVENT_NODE_REMOVED:
			case NODE_EVENT_CONFIG_CHANGED:
			case CNTRL_EVENT_INIT_COMPLETED:
				update();
				break;
			default:
				break;
		}
	}
	
	
	private void update(){
		DefaultListModel<JWaveNode> model = new DefaultListModel<JWaveNode>();
		
		if (cntrl != null) {
		  List<JWaveNode> nodes = cntrl.getNodes();
		  for (JWaveNode node : nodes){
			  model.addElement(node);
	      }
		}
				
		listNodes.setModel(model);
	}
	
	private void initGui(){
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		listNodes = new JList<JWaveNode>();
		listNodes.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){					
					panelNodeDetails.setNode((JWaveNode)listNodes.getSelectedValue());
					if (selectionListener != null){
						selectionListener.onNodeSelected((JWaveNode)listNodes.getSelectedValue());
					}
				}
			}
		});
		listNodes.setCellRenderer(new NodeRenderer());
		scrollPane.setViewportView(listNodes);
		
		panelNodeDetails = new PanelNodeDetails();
		splitPane.setRightComponent(panelNodeDetails);
		panelNodeDetails.setLayout(new CardLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 50));
		add(panel, BorderLayout.NORTH);
		
		JButton btnAddNode = new JButton("add Node");
		btnAddNode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cntrl.setInclusionMode();
			}
		});
		
		JButton btnAddnode = new JButton("remove Node");
		btnAddnode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cntrl.setExlusionMode();
			}
		});
		
		JButton btnNwi = new JButton("NWI");
		btnNwi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cntrl.setNetworkWideInclusionMode();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAddNode)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAddnode)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNwi)
					.addContainerGap(163, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAddNode)
						.addComponent(btnAddnode)
						.addComponent(btnNwi))
					.addContainerGap(16, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}
	
	private JList<JWaveNode> listNodes;
	private PanelNodeDetails panelNodeDetails;
	
	
	private class NodeRenderer extends DefaultListCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 5504819194985457333L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			JWaveNode node = (JWaveNode)value;
			if (c instanceof JLabel){
				String genStr;
				if (node.getGenericDeviceType() != null){
					genStr = node.getGenericDeviceType().getHelp();
				} else {
					genStr = " -- ";
				}			
				
				
				String nodeStr = ""+node.getNodeId();
				while (nodeStr.length() < 3){
					nodeStr = "0"+nodeStr;
				}
				
				
				
				((JLabel)c).setText("node "+nodeStr + "  |  "+genStr);
			}
			return c;
		}
	}
}
