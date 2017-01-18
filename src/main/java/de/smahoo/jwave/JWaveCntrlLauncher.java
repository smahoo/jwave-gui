package de.smahoo.jwave;

import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.JWaveControllerMode;
import de.smahoo.jwave.cmd.JWaveCommand;
import de.smahoo.jwave.cmd.JWaveCommandClass;
import de.smahoo.jwave.cmd.JWaveNodeCommand;
import de.smahoo.jwave.event.JWaveEvent;
import de.smahoo.jwave.event.JWaveEventListener;
import de.smahoo.jwave.node.JWaveNode;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.StringTokenizer;

public class JWaveCntrlLauncher implements Runnable {

	private static final String VERSION = "1.0.0";
	
	private static JWaveCntrlLauncher instance = null;
	private static JWaveController cntrl = null;
	private static String currentSerialPort = null;
	private static boolean keepAlive = true;
	private static String configFile;
	private CommPort commPort = null;
	
	public void run(){
		
		cntrl = new JWaveController();
		
		System.out.println("Using libzwave.jar v"+JWaveController.getVersion());
		
		JWaveController.doLogging(true);
		if (cntrl == null){
			System.out.println("Unable to initialize Controller");
			return;
		}
		if (cntrl.getCommandClassSpecifications() == null){				
			// mist
			System.out.println("Controller was not initialized with zwave specifications. Will exit now");
			return;
		}
		
		cntrl.addCntrlListener(new JWaveEventListener() {
			
			
			public void onJWaveEvent(JWaveEvent event) {
				handleJWaveEvent(event);
				
			}
		});
				
		System.out.println("please type command (type help for command list)");
		System.out.print("> ");
		while (keepAlive){
			try {
				Thread.sleep(1000);
			} catch (Exception exc){
				exc.printStackTrace();
			}
		}
		System.out.println("JWaveControllerLauncher will stop now");		
		try {
			//cntrl.cleanUp();
			cntrl.dispose();
			commPort.close();
		} catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	protected void handleJWaveEvent(JWaveEvent event){
		
	}
	
	protected void connect(String port){		
			
			int baudrate = 115200;
			
			CommPortIdentifier portIdentifier = null;
		    
			try {
		        	portIdentifier = CommPortIdentifier.getPortIdentifier(port);
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
		                currentSerialPort = port;
		              
		               
		            }  else  {
		            	
		         
		            }
		    	} catch (Exception exc){
		    		exc.printStackTrace();
		    	}
		   }  
		}
	
	
	
	
	protected static void printNodes(){
		if (cntrl==null){
			System.out.println("Controller is not initialized. Unable to print nodes.");
			return;
		}
		
		if (cntrl.getNodes().size() <=1){
			System.out.println("No nodes connected to this controller");
			return;
		}
		
		for (JWaveNode node : cntrl.getNodes()){
			System.out.println("");
			printNode(node);
		}
	}
	
	protected static void printNode(int id){
		for (JWaveNode node : cntrl.getNodes()){
			if (node.getNodeId() == id){
				printNode(node);
				return;
			}
		}
		System.out.println("There exists no node with id = "+id);
	}
	
	protected static void printNode(JWaveNode node) {
		System.out.println("===========================================================================");		
		System.out.println("             NODE "+node.getNodeId()+" | 0x"+Integer.toHexString(node.getGenericDeviceType().getKey())+" | "+node.getGenericDeviceType().getName());
		System.out.println("---------------------------------------------------------------------------");
		System.out.println("    Manufacturer 0x"+Integer.toHexString(node.getManufactureId()));
		System.out.println("    Product Type 0x"+Integer.toHexString(node.getProductTypeId()));
		System.out.println("         Product 0x"+Integer.toHexString(node.getProductId()));		
		System.out.println("---------------------------------------------------------------------------");
		System.out.println(" COMMAND CLASSES");
		for (JWaveCommandClass cc : node.getCommandClasses()){
			System.out.println("   0x"+Integer.toHexString(cc.getKey())+" "+cc.getName());
		}		
		System.out.println("---------------------------------------------------------------------------");
	}
	
	
	public static void evaluateParamCmd(String cmd){
		StringTokenizer tok = new StringTokenizer(cmd," ");
		String[] pcmd = new String[tok.countTokens()];
		for (int i = 0; i<pcmd.length; i++){
			pcmd[i] = tok.nextToken();
		}
		
		if ("send".equals(pcmd[0])){
			evalSendCmd(pcmd);
			return;
		}
		if ("connect".equalsIgnoreCase(pcmd[0])){
			evalConnectCmd(pcmd);
			return;
		}		
		if ("print".equalsIgnoreCase(pcmd[0])){
			evalPrintCmd(pcmd);
			return;
		}
		if ("set".equalsIgnoreCase(pcmd[0])){
			evalSetCmd(pcmd);
			return;
		}
		
		System.out.println("Unknown command ("+pcmd[0]+").");
	}
	
	
	protected static void evalSetCmd(String[] cmd){
		if (cmd.length < 2){
			System.out.println("Invalid set cmd");
		}
		if ("inclusion".equalsIgnoreCase(cmd[1])){
			System.out.println("Setting inclusion mode");
			cntrl.setInclusionMode(true);		
			return;
		}
		if ("reset".equalsIgnoreCase(cmd[1])){
			System.out.println("Resetting the controller");
			cntrl.resetController();
			return;
		}
		if ("exclusion".equalsIgnoreCase(cmd[1])){
			System.out.println("Setting exclusion mode");
			cntrl.setExlusionMode();
			return;
		}
		if ("normal".equalsIgnoreCase(cmd[1])){
			System.out.println("Setting controller back to normal mode");
			cntrl.setNormalMode();
			return;
		}
		
		System.out.println("Unknown set command ("+cmd[1]+")");
	}
	
	protected static void evalConnectCmd(String[] cmd){
		if (cmd.length < 2){
			System.out.println("Unvalid connect command");
		}
		System.out.println("Connecting to "+cmd[1]);
		instance.connect(cmd[1]);
	}
	
	public static void evalPrintCmd(String[] cmd){
		if (cmd.length < 2){
			System.out.println("Invalid print command");
			return;
		}
		
		if ("version".equalsIgnoreCase(cmd[1])){
			System.out.println("v"+VERSION);
			return;
		}
		if ("serial".equalsIgnoreCase(cmd[1])){
			printPorts();
			return;
		}
		if ("commands".equalsIgnoreCase(cmd[1])){
			printHelp();
			return;
		}
		if ("nodes".equalsIgnoreCase(cmd[1])){
			printNodes();
			return;
		}
		if ("node".equalsIgnoreCase(cmd[1])){
			if (cmd.length == 3){
				printNode(parseInt(cmd[2]));
			} else {
				System.out.println("Invalid print node command");
			}
			return;
		}
		if ("controller".equalsIgnoreCase(cmd[1])){
			printControllerDetails();
			return;
		}
		System.out.println("Unknown print command ("+cmd[1]+")");
	}
	
	protected static void printControllerDetails(){
		if (cntrl != null){
			if (cntrl.getControllerMode() == JWaveControllerMode.CNTRL_MODE_NOT_CONNECTED){
				System.out.println("Z-Wave Controller is not connected. Connect the controller to a serial port (use cmd \"connect <portname>\"");
				return;
			}
			System.out.println("--------------------------------------------------"+"\r\n"+
							   "                  Controller Details"+"\r\n"+
							   "--------------------------------------------------"+"\r\n"+
							   ""+"\r\n"+
							   "          connected to port = "+currentSerialPort+"\r\n"+
							   "             z-wave home id = "+cntrl.getHomeId()+"\r\n"+
							   "  z-wave controller version = "+cntrl.getControllerVersion()+"\r\n"+
							   "        z-wave chip version = "+cntrl.getZWaveChipVersion()+"\r\n"+
							   "--------------------------------------------------"+"\r\n");					
		}
	}
	
	protected static void printPorts(){
		System.out.println("available serial ports:");
		
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
		while (ports.hasMoreElements()){
			System.out.println("   "+ports.nextElement().getName());
		}
	}
	
	
	public static void evalSendCmd(String[] cmd){
		int version = 1;	
		boolean containsVersionParam = false;
		int nodeId;
		JWaveNode node = null;
		JWaveCommand zwaveCmd = null;
		if (cmd.length < 4){
			System.out.println("Unvalid send command -> send <id> <cmd_class_id> <cmd_id> [[param_value]]");
			return;
		}
		
		try {
			nodeId = Integer.parseInt(cmd[1]);
			
		} catch (Exception exc){
			System.out.println("Unvalid node Id ("+exc.getMessage()+")");
			return;
		}	
		
		node = cntrl.getNode(nodeId);
		if (node == null){
			System.out.println("There exists no node with id "+nodeId);
			return;
		}
	
		
		if (cmd.length > 4){
			if (cmd[4].contains("-v=")){
				containsVersionParam = true;
				try {
					version = Integer.parseInt(cmd[4].replace("-v=",""));
				} catch (Exception exc){
					System.out.println("Unvalid version parameter ("+cmd[4]+")");
				}
			}
		}
		
		zwaveCmd = getNodeCmd(cmd[2], cmd[3],version);
		
		
		if (zwaveCmd == null){
			System.out.println("Unable to find Z-Wave Command "+cmd[2]+" "+cmd[3]+" of version "+version);
			return;
		}		
			
		
		JWaveNodeCommand nodeCmd = new JWaveNodeCommand(zwaveCmd);
		
		
		int paramStart;
		if (containsVersionParam){
			paramStart = 5;
		} else {
			paramStart = 4;
		}
		
		if (paramStart < cmd.length){
			for (int i = paramStart; i< cmd.length; i++){
				try {
					nodeCmd.setParamValue(i-paramStart, parseInt(cmd[i]));
					
				} catch (Exception exc){
					System.out.println("Unable to set param value ("+(i-paramStart)+" "+cmd[i]+")");
					return;
				}
			}
		} 
		
		node.sendData(nodeCmd);
		
		
	}
	
	protected static int parseInt(String value) throws NumberFormatException{
		if (value.contains("0x")){
			return Integer.parseInt(value.replace("0x",""),16);
		}
		return Integer.parseInt(value);
	}
	
	protected static JWaveCommand getNodeCmd(String cl, String cmd, int version){
		int class_key = -1;
		int cmd_key = -1;
		try {			
			class_key = parseInt(cl);	
			
		} catch (Exception exc){
			
		}
		try {			
			cmd_key = parseInt(cl);	
			
		} catch (Exception exc){
			
		}
		
		JWaveCommandClass cmdClass = null;
		
		if (class_key != -1){
			cmdClass = cntrl.getCommandClassSpecifications().getCommandClass(class_key,version);
		} else {
			cmdClass = cntrl.getCommandClassSpecifications().getCommandClass(cl,version);
		}
		
		if (cmdClass == null){
			return null;
		}
		
		JWaveCommand zwaveCmd = null;
		
		if (cmd_key == -1){
			zwaveCmd = cmdClass.getCommand(cmd);
		} else {
			zwaveCmd = cmdClass.getCommand(cmd_key);
		}
		
		return zwaveCmd;
	}
	
	public static void evalCmd(String cmd){
		if (cmd.contains(" ")){
			evaluateParamCmd(cmd);
			return;
		}
		if ("help".equalsIgnoreCase(cmd)){
			printHelp();
		}
		if ("save".equalsIgnoreCase(cmd)){
			System.out.println("Saving Nodes Configuration");
			try {
				cntrl.saveConfiguration(configFile);
			} catch (Exception exc){
				exc.printStackTrace();
			}
			return;
		}
		if ("load".equalsIgnoreCase(cmd)){
			System.out.println("loading Nodes Configuration");
			try {
				cntrl.loadConfiguration(configFile);
			} catch (Exception exc){
				exc.printStackTrace();
			}
			return;
		}		
		
		if ("exit".equalsIgnoreCase(cmd)){			
			keepAlive = false;
			return;
		}
		System.out.println("unknown command ("+cmd+"). Type 'print commands' for a list of possible commands.");
	}
	
	
	protected static void printHelp(){
		System.out.println("===============================================================================");
		System.out.println("                                  HELP");
		System.out.println("===============================================================================");
		System.out.println("");			
		System.out.println("       save = saving nodes configuration");
		System.out.println("       load = loading nodes configuration");
		System.out.println("");
		System.out.println("        set = sets parameter");
		System.out.println("              ==> use: set <command>");
		System.out.println("              set inclusion    = sets controller to inclusion mode");
		System.out.println("              set exclusion    = sets controller to exclusion mode");
		System.out.println("              set normal       = sets controller to normal mode");
		System.out.println("              set reset        = resets the controller");		
		System.out.println("");
		System.out.println("    connect = connect with z-wave controller");		
		System.out.println("              ==> use: connect <portname>");		
		System.out.println("");
		System.out.println("       send = sends a command to a node");		
		System.out.println("              ==> use: send <id> <cmd_class> <cmd> [-v=<version>] [[param_value]]");
		System.out.println("");
		System.out.println("      print = prints something on the console");		
		System.out.println("              ==> use: print <what to print> [[additional params]]");
		System.out.println("              print commands   = prints this help");
		System.out.println("              print serial     = prints all available serial ports");
		System.out.println("              print version    = prints the version of this class");
		System.out.println("              print nodes      = prints alle node details");
		System.out.println("              print node <id>  = prints the node details of specific node");
		System.out.println("			  print controller = prints details about the current z-wave controller");		
		System.out.println("===============================================================================");
	}
	
	
	public static void main(String[] args){		
		System.out.println("");
		System.out.println("======================================================================================");
		System.out.println("  JWaveControllerLauncher v"+VERSION+" - sending commands directly to the z-wave controller");
		System.out.println("======================================================================================");		
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		configFile = System.getProperty("user.dir")+System.getProperty("file.separator")+"cnf"+System.getProperty("file.separator")+"nodes.xml";
		instance = new JWaveCntrlLauncher();
		Thread t = null;
	    try {
	         t = new Thread(instance);
	         t.start();
	    } catch (Exception exc){
	    	exc.printStackTrace();
	    }
	    	    
	    BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		
		String cmd = null;
		
	    while (keepAlive) {
	    	try {	    		
	    		cmd = console.readLine();
	    		if (cmd.length()!= 0){	    						
	    			evalCmd(cmd);
	    			System.out.print("> ");
	    		}
	    		
	    	} catch (Exception exc){
	    		exc.printStackTrace();
	    	}
	    }
	  
	    
	}
	
}
