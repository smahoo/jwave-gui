package de.smahoo.jwave.gui;

import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.CardLayout;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import de.smahoo.jwave.cmd.JWaveCommandClass;

@SuppressWarnings("serial")
public class PanelCommandClassList extends JPanel{
	
	private List<JWaveCommandClass> ccList = null;
	private SelectionListener selectionListener = null;

	public PanelCommandClassList(){
		
		initGui();
		update();
	}
	
	public void addSelectionListener(SelectionListener listener){
		selectionListener = listener;
	}
	
	public void setCommandList(List<JWaveCommandClass> ccList){
		this.ccList = ccList;
		update();
	
	}
	
	private void update(){
		updateList();
	}
	
	private void updateList(){
		DefaultListModel<JWaveCommandClass> model = new DefaultListModel<JWaveCommandClass>();
		if (ccList != null){
			
			for (JWaveCommandClass cc : ccList){
				model.addElement(cc);
			}
		
		}
		
		lstCmdClasses.setModel(model);
	}
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_27071047373316");
		
		lstCmdClasses = new JList<JWaveCommandClass>();
		lstCmdClasses.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					if (selectionListener != null){
						selectionListener.onCommandClassSelected((JWaveCommandClass)lstCmdClasses.getSelectedValue());
					}
				}
			}
		});
		
	
		lstCmdClasses.setCellRenderer(new CmdClassListRenderer());
		scrollPane.setViewportView(lstCmdClasses);
	}	
	
	
	private JList<JWaveCommandClass> lstCmdClasses;
	
	
	private class CmdClassListRenderer extends DefaultListCellRenderer{
		/**
		 * 
		 */
		private static final long serialVersionUID = 848947440012408269L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			JWaveCommandClass cc = (JWaveCommandClass)value;
			if (c instanceof JLabel){
				((JLabel)c).setText(cc.getName()+"_V"+cc.getVersion());
			}
			return c;
		}
	}
}
