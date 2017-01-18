package de.smahoo.jwave.gui;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;

import de.smahoo.jwave.cmd.*;
import de.smahoo.jwave.node.JWaveNode;

@SuppressWarnings("serial")
public class PanelSendDatagram extends JPanel{
	
	private JWaveNode node = null;
	private JWaveCommandClass currCmdClass = null;
	private JWaveCommand currCmd = null;
	
	public PanelSendDatagram(){		
		initGui();
		update();
	}
	

	public void setNode(JWaveNode node){
		this.node = node;
		update();
	}
	
	private void sendNodeCmd(){
		if (node == null) return;
		if (currCmd == null) return;
		JWaveNodeCommand nodeCmd = new JWaveNodeCommand(currCmd);
		
		HashMap<JWaveCommandParameter,String> vals = ((ParamValueTableModel)tblValues.getModel()).getValues();
	
		Set<Entry<JWaveCommandParameter,String>> entries = vals.entrySet();
	
		for (Entry<JWaveCommandParameter,String> entry : entries){
			try {
				nodeCmd.setParamValue(entry.getKey(),entry.getValue());
			} catch (Exception exc){
				exc.printStackTrace();
			}
		}	
		
		if ((node.supportsClassMultiCmd()) && (chckbxUseMultiCommand.isSelected())){
			JWaveNodeCommand cmdToSend = nodeCmd;
			JWaveCommand cmdEncap = getMultiEncapCmd();
			if (cmdEncap != null){
				nodeCmd = new JWaveMultiNodeCommand(cmdEncap);
				((JWaveMultiNodeCommand)nodeCmd).addJWaveNodeCmd(cmdToSend);
			}
		}
		
		try {
			node.sendData(nodeCmd);
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	protected JWaveCommand getMultiEncapCmd(){
		for (JWaveCommandClass cmdClass: node.getCommandClasses()){
			if (cmdClass.getKey() == 0x8f){
				return cmdClass.getCommand(0x01);
			}
		}
		return null;
	}
	
	private void update(){
		setCommandClasses();
		setCommands();
		updateTable();
		if (node != null){
			chckbxUseMultiCommand.setEnabled(node.supportsClassMultiCmd());
		} else {
			chckbxUseMultiCommand.setEnabled(false);
		}
		btnSend.setEnabled(currCmd != null);
	}
	
	private void updateTable(){
		TableModel model = null;
		if (currCmd != null){
			model = new ParamValueTableModel(currCmd.getParamList());
		} else {
			model = new ParamValueTableModel(null);
		}
		tblValues.setModel(model);
		
	}
	
	private void setCommands(){
		cBoxCommand.removeAllItems();
		currCmd = null;
		if (currCmdClass == null) return;
		
		for (JWaveCommand cmd : currCmdClass.getCommandList()){
			cBoxCommand.addItem(cmd);
		}
		if (cBoxCommand.getItemCount() > 0){
			cBoxCommand.setSelectedIndex(0);
			currCmd = (JWaveCommand) cBoxCommand.getSelectedItem();
		}
	}
	
	private void setCommandClasses(){		
		cBoxCommandClass.removeAllItems();
		currCmdClass = null;
		if (node == null) return;
		
		for (JWaveCommandClass cmdClass : node.getCommandClasses()){
			cBoxCommandClass.addItem(cmdClass);
		}
		if (cBoxCommandClass.getItemCount() > 0){
			cBoxCommandClass.setSelectedIndex(0);
			currCmdClass = (JWaveCommandClass) cBoxCommandClass.getSelectedItem();
		}
	}
	
	
	private void onCmdClassSelected(JWaveCommandClass cmdClass){
		currCmdClass = cmdClass;
		setCommands();
		updateTable();
	}
	
	private void onCmdSelected(JWaveCommand cmd){
		currCmd = cmd;
		updateTable();
	}
	
	private void initGui(){
		cBoxCommandClass = new JComboBox<JWaveCommandClass>();
		cBoxCommandClass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onCmdClassSelected((JWaveCommandClass) cBoxCommandClass.getSelectedItem());
				}
			}
		});
		
		
		cBoxCommandClass.setRenderer(new CmdListCellRenderer());
		JLabel lblNewLabel = new JLabel("Command Class");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblNewLabel_1 = new JLabel("Command");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
		
		cBoxCommand = new JComboBox<JWaveCommand>();
		cBoxCommand.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					onCmdSelected((JWaveCommand)cBoxCommand.getSelectedItem());
				}
			}
		});
		cBoxCommand.setRenderer(new CmdListCellRenderer());
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendNodeCmd();
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		chckbxUseMultiCommand = new JCheckBox("use multi command");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblNewLabel_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblNewLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(cBoxCommand, 0, 328, Short.MAX_VALUE)
								.addComponent(cBoxCommandClass, 0, 328, Short.MAX_VALUE)))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(chckbxUseMultiCommand)
							.addPreferredGap(ComponentPlacement.RELATED, 276, Short.MAX_VALUE)
							.addComponent(btnSend))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(cBoxCommandClass, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel_1)
						.addComponent(cBoxCommand, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
					.addGap(9)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSend)
						.addComponent(chckbxUseMultiCommand))
					.addContainerGap())
		);
		
		tblValues = new JTable();
		scrollPane.setViewportView(tblValues);
		setLayout(groupLayout);
	}
	private JTable tblValues;
	private JComboBox<JWaveCommandClass> cBoxCommandClass;
	private JComboBox<JWaveCommand> cBoxCommand;
	private JButton btnSend;
	private JCheckBox chckbxUseMultiCommand;
	
	public class CmdListCellRenderer extends DefaultListCellRenderer{
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,	int index, boolean isSelected, boolean cellHasFocus) {
			Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			
			if (renderer instanceof JLabel){
				JLabel l = (JLabel)renderer;
				if (value instanceof JWaveCommandClass){
					l.setText(""+((JWaveCommandClass)value).getName());
				}
				if (value instanceof JWaveCommand){
					l.setText(""+((JWaveCommand)value).getName());
				}
			}					
			return renderer;			
		}
	}
	
	public class ParamValueTableModel extends DefaultTableModel{		
		private String[][] paramVal = null;
		private List<JWaveCommandParameter> paramList = null;
		
		public ParamValueTableModel(List<JWaveCommandParameter> paramList){
			super();
			this.paramList = paramList;
			if (paramList == null) return;
			
			paramVal = new String[paramList.size()][2];
			
			for (int i = 0; i<paramList.size(); i++){
				paramVal[i][0] = paramList.get(i).getName();
				paramVal[i][1] = getDefaultValue(paramList.get(i));
			}
		}
		
		public HashMap<JWaveCommandParameter, String> getValues(){
			HashMap<JWaveCommandParameter,String> map = new HashMap<JWaveCommandParameter, String>();
			
			for (int i = 0; i<paramList.size(); i++){
				map.put(paramList.get(i), paramVal[i][1]);
			}
			
			return map;
		}
		
		private String getDefaultValue(JWaveCommandParameter param){
			String res = "";
			for (int i = 0; i<JWaveCommandParameterType.getSize(param.getType()); i++){
				if (res.length() > 0){
					res = res + " 00";	
				} else {
					res = "00";
				}
				
			}
			return res;
		}
		
		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			if (paramVal == null) return;
			if (columnIndex == 1){
				paramVal[rowIndex][columnIndex] = (String)aValue;
			}
		}		
		
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return columnIndex == 1;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (paramVal == null) return null;
			return paramVal[rowIndex][columnIndex];			
		}
		
		@Override
		public int getRowCount() {		
			if (paramList == null) return 0;
			return paramList.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {			
			if (columnIndex == 0){
				return "Param";
			}
			return "Value";
		}
		
		@Override
		public int getColumnCount() {		
			return 2;
		}
			
	}
}
