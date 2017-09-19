package de.smahoo.jwave.gui;

import de.smahoo.jwave.ExtendedJWaveCntrl;
import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.cmd.JWaveCommand;
import de.smahoo.jwave.cmd.JWaveCommandClass;
import de.smahoo.jwave.cmd.JWaveCommandClassSpecification;
import de.smahoo.jwave.cmd.JWaveNodeCommand;
import de.smahoo.jwave.event.JWaveEvent;
import de.smahoo.jwave.event.JWaveEventListener;
import de.smahoo.jwave.event.JWaveNodeDataEvent;
import de.smahoo.jwave.event.JWaveNodeEvent;
import de.smahoo.jwave.node.JWaveNode;
import de.smahoo.jwave.specification.JWaveSpecification;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class FrameTestZWave extends JFrame{

	protected JWaveController cntrl = null;
	protected SelectionListener selectionListener;
	protected HashMap<JWaveNode, List<JWaveNodeCommand>> nodeCmdLists;
	//protected JWaveNodeEvent currentNode = null;
	
	protected FrameAddNodeToNetworkProgress frameAddNode = null;
	protected FrameRemoveNodeFromNetworkProgress frameRemoveNode = null;
	protected FrameNodeAlreadyAdded frameNodeAlreadyAdded = null;
	
	public FrameTestZWave(JWaveController cntrl){
		super();		
		JWaveController.doLogging(true);
		this.cntrl = cntrl;
		init();
	}
	
	public FrameTestZWave(){
		super();
		JWaveController.doLogging(true);
		try {
			
			//cntrl = new TestZWaveCntrl();  // is not working for Mac -> specification file needs to be given to constructor
			//String specPath = System.getProperty("user.dir")+System.getProperty("file.separator")+"cnf"+System.getProperty("file.separator")+"cmd_classes.xml";
			//JWaveCommandClassSpecification cmdClassSpec = new JWaveCommandClassSpecification(specPath);
			JWaveCommandClassSpecification cmdClassSpec = JWaveSpecification.loadDefaultSpecification();
			cntrl = new ExtendedJWaveCntrl(cmdClassSpec);
			
		} catch (Exception exc){
			cntrl = new ExtendedJWaveCntrl(null);
		}
		init();
	}
	
	private void init(){
		nodeCmdLists = new HashMap<JWaveNode, List<JWaveNodeCommand>>();
		selectionListener = new SelectionListener() {
			
			@Override
			public void onNodeSelected(JWaveNode node) {
				// TODO Auto-generated method stub
				evaluateNodeSelection(node);
			}
			
			@Override
			public void onCommandSelected(JWaveCommand cmd) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onCommandClassSelected(JWaveCommandClass cmdClass) {
				// TODO Auto-generated method stub
				
			}
		};
		
		cntrl.setSecurityEnabled(false);
		cntrl.addCntrlListener(new JWaveEventListener() {
			
			@Override
			public void onJWaveEvent(JWaveEvent event) {
				evaluateJWaveEvent(event);
				
			}
		});
	//	cntrl.setControllerModeTimeout(15000);
		setLookAndFeel();
		initGui();
	}
	
	private void setLookAndFeel(){
		try {            
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception exc) {
			// unable to set Look & Feel
		}

	}
	
	private void loadCmdClasses(){
		
		String filename = System.getProperty("user.dir")+System.getProperty("file.separator")+"cnf"+System.getProperty("file.separator")+"cmd_classes.xml";
		
		JWaveCommandClassSpecification def = null;
		try {
			def = new JWaveCommandClassSpecification(filename);
		} catch (Exception exc){
			exc.printStackTrace();
		}
		FrameCommandClasses frameCc = new FrameCommandClasses();
		frameCc.setDefinitions(def);
		frameCc.setVisible(true);
	}
	
	private void saveConfiguration(){
		String path = System.getProperty("user.dir");
		path = path+System.getProperty("file.separator")+"zwave.cnf";
		try {
			cntrl.saveConfiguration(path);
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	private void loadConfiguration(){
		String path = System.getProperty("user.dir");
		path = path+System.getProperty("file.separator")+"zwave.cnf";
		try {
			cntrl.loadConfiguration(path);
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	private void evaluateJWaveEvent(JWaveEvent event){
		switch(event.getEventType()){
		case CNTRL_EVENT_ADD_NODE_TO_NETWORK_START:
			if (frameAddNode == null){
				frameAddNode = new FrameAddNodeToNetworkProgress(this.cntrl);				
			}
			frameAddNode.setVisible(true);
			break;
		case CNTRL_EVENT_ADD_NODE_TO_NETWORK_CANCELED:
		case CNTRL_EVENT_ADD_NODE_TO_NETWORK_FAILED:
			frameAddNode.setVisible(false);
			
			break;
		case CNTRL_EVENT_ADD_NODE_TO_NETWORK_SUCCESS:
				frameAddNode.setVisible(false);
			break;
		case CNTRL_EVENT_REMOVE_NODE_FROM_NETWORK_START:
			 if (frameRemoveNode == null){
				 frameRemoveNode = new FrameRemoveNodeFromNetworkProgress(cntrl);
			 }
			 frameRemoveNode.setVisible(true);
			break;
		case CNTRL_EVENT_REMOVE_NODE_FROM_NETWORK_CANCELED:
		case CNTRL_EVENT_REMOVE_NODE_FROM_NETWORK_FAILED:
		case CNTRL_EVENT_REMOVE_NODE_FROM_NETWORK_SUCCESS:
			 frameRemoveNode.setVisible(false);
			break;
		case NODE_EVENT_NODE_ALREADY_ADDED:
			if (frameNodeAlreadyAdded == null){
				 frameNodeAlreadyAdded= new FrameNodeAlreadyAdded();	
			}
			frameNodeAlreadyAdded.setVisible(true);
			break;
		case CNTRL_EVENT_CONTROLLER_RESET:
		case CNTRL_EVENT_INIT_COMPLETED:
			// set Controller Informations
		case NODE_EVENT_DATA_RECEIVED:
		case NODE_EVENT_DATA_SENT:
			if (event instanceof JWaveNodeDataEvent){
				JWaveNodeDataEvent nodeEvent = (JWaveNodeDataEvent)event;
				List<JWaveNodeCommand> dList = nodeCmdLists.get(nodeEvent.getNode());
				if (dList == null){
					dList = new ArrayList<JWaveNodeCommand>();
					nodeCmdLists.put(nodeEvent.getNode(),dList);
				}
				if (!dList.contains(nodeEvent.getNodeCmd())){
					dList.add(nodeEvent.getNodeCmd());
				}
			}
			break;
			default:
				break;
		}
		if (event instanceof JWaveNodeEvent){
			panelNodeList.invalidate();			
			panelNodeCmdList.invalidate();
		}
	}
	
	private void evaluateNodeSelection(JWaveNode node){
		this.panelSendDatagram.setNode(node);
		List<JWaveNodeCommand> cmdList = null;
		if (node != null){
			 cmdList = nodeCmdLists.get(node);
			 if (cmdList == null){
					cmdList = new ArrayList<JWaveNodeCommand>();
					nodeCmdLists.put(node,cmdList);
			}
		}
		this.panelNodeCmdList.setCmdList(cmdList);
	}
	
	
	private void initGui(){	
		
		setSize(new Dimension(1200, 1000));
		JPanel pnlTop = new JPanel();
		pnlTop.setPreferredSize(new Dimension(10, 75));
		pnlTop.setSize(new Dimension(0, 30));
		getContentPane().add(pnlTop, BorderLayout.NORTH);
		
		JButton btnInitCntrl = new JButton("Init Controller");
		btnInitCntrl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initController();
			}
		});
		
		JButton btnLoadCmdClasses = new JButton("load cmd classes");
		btnLoadCmdClasses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadCmdClasses();			
			}
		});
		
		JButton btnSaveConfiguration = new JButton("Save Configuration");
		btnSaveConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfiguration();
			}
		});
		
		JButton btnLoadConfiguration = new JButton("Load Configuration");
		btnLoadConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadConfiguration();
			}
		});
		
		JButton btnResetController = new JButton("Reset Controller");
		btnResetController.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cntrl.resetController();
			}
		});
		
		JLabel lblNewLabel = new JLabel("Comm Port");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		cboxCommPorts = new JComboBox<String>();
		cboxCommPorts.setMinimumSize(new Dimension(100, 40));
		cboxCommPorts.setSize(new Dimension(52, 35));
		cboxCommPorts.setPreferredSize(new Dimension(100, 35));
		
		JButton btnNewButton = new JButton("Get RSSI Background");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//	FrameDatagramSimulation sim = new FrameDatagramSimulation(cntrl);
			//	sim.setVisible(true);
			//	cntrl.sendRssiCmd();
			}
		});
	
		GroupLayout gl_pnlTop = new GroupLayout(pnlTop);
		gl_pnlTop.setHorizontalGroup(
			gl_pnlTop.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlTop.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlTop.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnInitCntrl, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_pnlTop.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cboxCommPorts, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGap(27)
					.addComponent(btnLoadCmdClasses, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)
					.addGap(27)
					.addGroup(gl_pnlTop.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnSaveConfiguration, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnLoadConfiguration, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnResetController)
					.addPreferredGap(ComponentPlacement.RELATED, 294, Short.MAX_VALUE)
					.addComponent(btnNewButton)
				
					.addContainerGap())
		);
		gl_pnlTop.setVerticalGroup(
			gl_pnlTop.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlTop.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlTop.createParallelGroup(Alignment.LEADING)
						.addComponent(btnLoadCmdClasses, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_pnlTop.createSequentialGroup()
							.addGroup(gl_pnlTop.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_pnlTop.createParallelGroup(Alignment.BASELINE)
									.addComponent(lblNewLabel)
									.addComponent(cboxCommPorts, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_pnlTop.createParallelGroup(Alignment.BASELINE)
									.addComponent(btnResetController)
									.addComponent(btnLoadConfiguration)
									.addComponent(btnNewButton)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_pnlTop.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnInitCntrl)
								.addComponent(btnSaveConfiguration))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlTop.setLayout(gl_pnlTop);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		getContentPane().add(splitPane, BorderLayout.CENTER);
		
		PanelLog panelLog = new PanelLog(cntrl);
		splitPane.setRightComponent(panelLog);
		
		JSplitPane splitPaneNode = new JSplitPane();
		splitPaneNode.setResizeWeight(0.5);
		splitPane.setLeftComponent(splitPaneNode);
		
		panelNodeList = new PanelNodeList();
		panelNodeList.setPreferredSize(new Dimension(260, 350));
		panelNodeList.setCntrl(cntrl);		
		panelNodeList.setSelectionListener(selectionListener);
		splitPaneNode.setLeftComponent(panelNodeList);
		
		JSplitPane splitPaneDatagram = new JSplitPane();
		splitPaneDatagram.setResizeWeight(0.5);
		splitPaneDatagram.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPaneNode.setRightComponent(splitPaneDatagram);
		
		panelSendDatagram = new PanelSendDatagram();
		panelSendDatagram.setPreferredSize(new Dimension(10, 200));
		splitPaneDatagram.setLeftComponent(panelSendDatagram);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPaneDatagram.setRightComponent(tabbedPane);
		
		panelNodeCmdList = new PanelNodeCmdList();
		tabbedPane.addTab("Dialog", null, panelNodeCmdList, null);
		
		PanelNodeCmdList panelNodeCmdQueue = new PanelNodeCmdList();
		tabbedPane.addTab("Cmd Queue", null, panelNodeCmdQueue, null);
		setTitle("Testing ZWaveCntrl");
		listCommPorts();
		
	}
	
	protected void listCommPorts(){
		cboxCommPorts.removeAll();
		 @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
	     while ( portEnum.hasMoreElements() )    {
	            CommPortIdentifier portIdentifier = portEnum.nextElement();
	            cboxCommPorts.addItem(portIdentifier.getName());	            
	        }  
	}
	
	protected void initController(){
		connect();
	}
	
	protected void connect(){		
	//	String portName = "/dev/cu.usbmodem1431";
		String portName = (String)cboxCommPorts.getSelectedItem();
		int baudrate = 115200;
		CommPort commPort;		
		CommPortIdentifier portIdentifier = null;
	    
		try {
	        	portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
	    } catch (NoSuchPortException exc){
	        	exc.printStackTrace();
	    }
	    
		if ( portIdentifier.isCurrentlyOwned() ) {
	            System.out.println("Error: Port is currently in use");
	    } else   {
	    	try {
	          commPort = portIdentifier.open(this.getClass().getName(),2000);
	            
	            if ( commPort instanceof SerialPort )  {
	                SerialPort serialPort = (SerialPort) commPort;
	                serialPort.setSerialPortParams(baudrate,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);                    
	                serialPort.enableReceiveTimeout(500000);          
	                
	                cntrl.init(serialPort.getInputStream(),serialPort.getOutputStream());
	                
	              
	               
	            }  else  {
	            	
	         
	            }
	    	} catch (Exception exc){
	    		exc.printStackTrace();
	    	}
	   }  
	}
	
	private PanelNodeList panelNodeList;
	private PanelSendDatagram panelSendDatagram;
	private PanelNodeCmdList panelNodeCmdList;
	private JComboBox<String> cboxCommPorts;
}
