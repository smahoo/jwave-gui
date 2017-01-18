package de.smahoo.jwave.gui;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.CardLayout;
import java.text.SimpleDateFormat;

import javax.swing.JTextArea;
import javax.swing.JTabbedPane;

import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.event.*;

import java.awt.BorderLayout;

public class PanelLog extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9037287310422828409L;
	@SuppressWarnings("unused")
	private JWaveController cntrl = null;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public PanelLog(JWaveController cntrl) {
		super();
		this.cntrl = cntrl;
		cntrl.addCntrlListener(new JWaveEventListener() {
			
			@Override
			public void onJWaveEvent(JWaveEvent event) {
				logEvent(event);
				
			}
		});
		initGui();
	}
	
	
	private void logEvent(JWaveEvent event){		
		txtAllEvents.append("["+formatter.format(event.getTimestamp())+"] "+event.getEventType().name()+"\r\n");
		if (event instanceof JWaveControlEvent){
			logCntrlEvent((JWaveControlEvent)event);
		}
		if (event instanceof JWaveNodeEvent){
			logNodeEvent((JWaveNodeEvent)event);
		}
		if (event instanceof JWaveDatagramEvent){
			logDatagramEvent((JWaveDatagramEvent)event);
		}
	}
	
	private void logCntrlEvent(JWaveControlEvent event){
		txtCntrlEvents.append("["+formatter.format(event.getTimestamp())+"] "+event.getEventType().name()+"\r\n");
	}
	
	private void logNodeEvent(JWaveNodeEvent event){
		txtNodeEvents.append("["+formatter.format(event.getTimestamp())+"] "+event.getEventType().name()+"   NodeId="+event.getNode().getNodeId()+"\r\n");
	}
	
	private void logDatagramEvent(JWaveDatagramEvent event){
		//txtDatagramEvents.append("["+formatter.format(event.getTimestamp())+"] "+event.getEventType().name()+"   "+event.getDatagram().getCommandType().name()+"   "+event.getDatagram().toHexString()+"\r\n");
		panelDatagramList.add(event.getDatagram());
	}
	
	private void initGui(){
		setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlAllEvents = new JPanel();
		tabbedPane.addTab("All Events", null, pnlAllEvents, null);
		pnlAllEvents.setLayout(new CardLayout(0, 0));
		
		scrollPane = new JScrollPane();
		pnlAllEvents.add(scrollPane, "name_10537830832568");
		
		txtAllEvents = new JTextArea();
		scrollPane.setViewportView(txtAllEvents);
		
		JPanel pnlCntrlEvents = new JPanel();
		tabbedPane.addTab("Controller Events", null, pnlCntrlEvents, null);
		pnlCntrlEvents.setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPaneCntrlEvents = new JScrollPane();
		pnlCntrlEvents.add(scrollPaneCntrlEvents, "name_10606416412224");
		
		txtCntrlEvents = new JTextArea();
		scrollPaneCntrlEvents.setViewportView(txtCntrlEvents);
		
		JPanel pnlDatagramEvents = new JPanel();
		tabbedPane.addTab("Datagram Events", null, pnlDatagramEvents, null);
		pnlDatagramEvents.setLayout(new CardLayout(0, 0));
		
		panelDatagramList = new PanelDatagramList();
		pnlDatagramEvents.add(panelDatagramList, "name_3763934227973");
		
		JPanel pnlNodeEvents = new JPanel();
		tabbedPane.addTab("Node Events", null, pnlNodeEvents, null);
		pnlNodeEvents.setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPaneNodeEvents = new JScrollPane();
		pnlNodeEvents.add(scrollPaneNodeEvents, "name_10774638646064");
		
		txtNodeEvents = new JTextArea();
		scrollPaneNodeEvents.setViewportView(txtNodeEvents);
	}

	private JScrollPane scrollPane;
	private JTextArea txtAllEvents;
	private JTextArea txtNodeEvents;
	private JTextArea txtCntrlEvents;
	private PanelDatagramList panelDatagramList;
	
}
