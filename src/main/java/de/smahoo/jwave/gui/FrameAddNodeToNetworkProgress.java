package de.smahoo.jwave.gui;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import de.smahoo.jwave.JWaveController;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

public class FrameAddNodeToNetworkProgress extends JDialog{

	JWaveController cntrl = null;
	private static final long serialVersionUID = 1L;
	
	public FrameAddNodeToNetworkProgress(JWaveController cntrl){
		super();
		
		this.cntrl = cntrl;
		initGui();
		update();
	}
	
	private void sendCancelCmd(){
		cntrl.setNormalMode();
	}
	
	private void update(){
		
	}
	
	private void initGui(){
		setAlwaysOnTop(true);
		setResizable(false);
		setSize(500,100);
		getContentPane().setPreferredSize(new Dimension(300, 200));
		setTitle("Add Node To Network");
		JLabel lblNewLabel = new JLabel("Please press the LRN button on your device.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendCancelCmd();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(177)
							.addComponent(btnCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(192)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
					.addComponent(btnCancel)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
}
