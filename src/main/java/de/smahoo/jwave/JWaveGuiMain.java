package de.smahoo.jwave;




import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;





import de.smahoo.jwave.gui.FrameTestZWave;



public class JWaveGuiMain implements Runnable{

	private FrameTestZWave mainFrame= null;
	

	public static void main(String[] args) {		
		// TODO Auto-generated method stub
				
		Runnable app = new JWaveGuiMain();	        
	    try {
	         SwingUtilities.invokeAndWait(app);
	    } catch (InvocationTargetException ex) {
	         ex.printStackTrace();
	    } catch (InterruptedException ex) {
	         ex.printStackTrace();
	    }
	}
	
	public void run(){
	   
		mainFrame = new FrameTestZWave();
	
		mainFrame.addWindowListener(new WindowAdapter() {		
	         public void windowClosing(WindowEvent e){		        
	           mainFrame.dispose();	      	 
	         }
	      });		
		mainFrame.setVisible(true);
		
		
		
	}

}
