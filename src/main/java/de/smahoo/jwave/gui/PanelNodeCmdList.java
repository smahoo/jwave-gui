package de.smahoo.jwave.gui;

import java.util.List;

import javax.swing.JPanel;

import java.awt.CardLayout;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import de.smahoo.jwave.cmd.JWaveNodeCommand;
public class PanelNodeCmdList extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7717967335719472728L;
	private List<JWaveNodeCommand> cmdList = null;
	
	
	public PanelNodeCmdList(){
		super();			
		initGui();
		update();
	}
	
	public void setCmdList(List<JWaveNodeCommand> cmdList){
		this.cmdList = cmdList;
		update();
	}
	
	@Override
	public void invalidate(){		
		super.invalidate();	
		tblNodeCmds.updateUI();
	}

	private void update(){
		NodeCmdTableModel model = new NodeCmdTableModel(cmdList);
		tblNodeCmds.setModel(model);
	}
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_32650826484113");
		
		tblNodeCmds = new JTable();
		scrollPane.setViewportView(tblNodeCmds);
		
	}
	
	private JTable tblNodeCmds;
	
	
	@SuppressWarnings("serial")
	protected class NodeCmdTableModel extends AbstractTableModel{		
		
		List<JWaveNodeCommand> cmdList = null;
			
		public NodeCmdTableModel(List<JWaveNodeCommand> cmdList){
			this.cmdList = cmdList;
		}
		
		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {		
			return false;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (cmdList == null) return "";
			if (cmdList.get(rowIndex) == null){
				return "";
			}
			switch(columnIndex){
			case 0:
				if (cmdList.get(rowIndex).isReceived()){
					return "--->";
				} else {
					return "<---";
				}
			case 1: 
				return ""+cmdList.get(rowIndex).getCommandClass().getName();
			case 2:
				return ""+cmdList.get(rowIndex).getCommand().getName();
			case 3:
				return "---";
			}
			return "";
		}
		
		@Override
		public int getRowCount() {
			if (cmdList == null) return 0;
			return cmdList.size();
		}
		
		@Override
		public String getColumnName(int columnIndex) {
			switch(columnIndex){
			case 0: return "Direction";
			case 1: return "Command Class";
			case 2: return "Command";
			case 3: return "Parameter";
			}
			return "--";
		}
		
		@Override
		public int getColumnCount() {			
			return 4;
		}
		
		
	}
}
