package de.smahoo.jwave.gui.simulation;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.smahoo.jwave.ExtendedJWaveCntrl;
import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.io.JWaveDatagram;
import de.smahoo.jwave.io.JWaveDatagramFactory;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

public class PanelDatagramSimulation extends JPanel{
	
	JWaveController cntrl;
	
	public PanelDatagramSimulation(JWaveController cntrl){
		super();		
		init();
		setZWaveCntrl(cntrl);
		
	}
	
	
	public void setZWaveCntrl(JWaveController cntrl){
		this.cntrl = cntrl;
		update();
	}
	
	protected void update(){
		
	}
	
	protected void evalBytes(){
		String str = txtCmdBytes.getText();
		StringTokenizer tok = new StringTokenizer(str," ");
		byte[] bytes = new byte[tok.countTokens()+1];
		int index = 0;
		
		while (tok.hasMoreElements()){
			bytes[index] = (byte)Integer.parseInt(tok.nextToken(),16);
			index++;
		}
		
		JWaveDatagram datagram = JWaveDatagramFactory.generateDatagram(bytes);
		
		try {
			((ExtendedJWaveCntrl)cntrl).simulateDatagram(datagram);
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}	
	
	private void init() {
		
		JScrollPane scrollPane = new JScrollPane();
		
		btnEvaluate = new JButton("simulate");
		btnEvaluate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				evalBytes();
			}
		});
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addComponent(btnEvaluate))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnEvaluate)
					.addContainerGap())
		);
		
		txtCmdBytes = new JTextPane();
		scrollPane.setViewportView(txtCmdBytes);
		setLayout(groupLayout);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextPane txtCmdBytes;
	private JButton btnEvaluate;
	
}
