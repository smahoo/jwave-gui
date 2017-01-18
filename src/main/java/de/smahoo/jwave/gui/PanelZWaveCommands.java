package de.smahoo.jwave.gui;

import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import de.smahoo.jwave.cmd.JWaveCommand;
@SuppressWarnings("serial")
public class PanelZWaveCommands extends JPanel  {
	
	private List<JWaveCommand> cmdList;
	private SelectionListener selectionListener = null;
	
	public PanelZWaveCommands(){
		
		initGui();
		update();
	}
	
	
	public void setCommandList(List<JWaveCommand> cmdList){
		this.cmdList = cmdList;
		update();
	}
	
	public void addSelectionListener(SelectionListener listener){
		selectionListener = listener;
	}
	
	private void update(){
		DefaultListModel<JWaveCommand> model= new DefaultListModel<JWaveCommand>();
		if (cmdList != null){
			for (JWaveCommand cmd : cmdList){
				model.addElement(cmd);
			}
		}
		
		lstCommands.setModel(model);
	}
	
	
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "name_30010526951094");
		
		lstCommands = new JList<JWaveCommand>();
		lstCommands.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()){
					if (selectionListener != null){
						selectionListener.onCommandSelected((JWaveCommand)lstCommands.getSelectedValue());
					}
				}
			}
		});
		lstCommands.setCellRenderer(new CmdListRenderer());
		scrollPane.setViewportView(lstCommands);
	}
	
	private class CmdListRenderer extends DefaultListCellRenderer{
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			JWaveCommand cmd = (JWaveCommand)value;
			if (c instanceof JLabel){
				((JLabel)c).setText(cmd.getName());
			}
			return c;
		}
	}
	
	private JList<JWaveCommand> lstCommands;
	
}
