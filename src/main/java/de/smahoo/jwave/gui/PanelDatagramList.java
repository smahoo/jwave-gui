package de.smahoo.jwave.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.CardLayout;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.smahoo.jwave.io.JWaveDatagram;
import de.smahoo.jwave.io.JWaveDatagramStatus;

@SuppressWarnings("serial")
public class PanelDatagramList extends JPanel {

	List<JWaveDatagram> lstDatagram = null;
	private JTable tblDatagrams;
	
	public PanelDatagramList(){
		
		initGui();
		update();
	}
	
	
	public void add(JWaveDatagram datagram){
		if (lstDatagram == null){
			lstDatagram = new ArrayList<JWaveDatagram>();
		}
		lstDatagram.add(datagram);
		tblDatagrams.invalidate();
	}
	
	public void setDatagramList(List<JWaveDatagram> datagramList){
		this.lstDatagram = datagramList;	
		update();
	}
	
	
	
	private void update(){
		tblDatagrams.setModel(generateTableModel());		
	}
	
	
	private TableModel generateTableModel(){
		TableModel tm = new DatagramTableModel(lstDatagram);	
		return tm;
	}
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_20156363288212");
		
		tblDatagrams = new JTable();
		scrollPane.setViewportView(tblDatagrams);
		lstDatagram = new ArrayList<JWaveDatagram>();
	}
	
	
	
	public class DatagramTableModel extends AbstractTableModel{
		
		protected List<JWaveDatagram> datagramList;
		
		public DatagramTableModel(List<JWaveDatagram> dlist){
			this.datagramList = dlist;
			
		}
		
		@Override
		public String getColumnName(int index){
			switch(index){
			case 0: 
				return "Direction";
			case 1:
				return "Status";
			case 2:
				return "Type";
			case 3:
				return "Raw Data";
			default:
				return ""+index;
			}
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			JWaveDatagram datagram = datagramList.get(rowIndex);
			
			if (datagram == null){
				return "--";
			}
			
			switch (columnIndex){
			case 0:
				if (datagram.getStatus() == JWaveDatagramStatus.STATUS_RECEIVED){
					return "<--";
				};
				
				if (datagram.getStatus() == JWaveDatagramStatus.STATUS_GENERATED){
					return "gen";
				}
				
				return "-->";
				
			case 1:
				return datagram.getStatus().name();
			case 2: 
				return datagram.getCommandType().name();
			case 3: 
				return datagram.toHexString();				
			}
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getRowCount() {
			if (datagramList == null) return 0;
			return datagramList.size();
		}
		
		@Override
		public int getColumnCount() {
			return 4;
		}
	}
	
}
