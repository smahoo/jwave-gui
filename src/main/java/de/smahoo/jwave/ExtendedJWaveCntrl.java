package de.smahoo.jwave;

import de.smahoo.jwave.JWaveController;
import de.smahoo.jwave.JWaveException;
import de.smahoo.jwave.cmd.JWaveCommandClassSpecification;
import de.smahoo.jwave.io.JWaveDatagram;
public class ExtendedJWaveCntrl extends JWaveController{
	
	
	public ExtendedJWaveCntrl(){
		super();
	}
	
	public ExtendedJWaveCntrl(JWaveCommandClassSpecification cmdClassSpec){
		super(cmdClassSpec);
	}
	
	public void simulateDatagram(JWaveDatagram datagram) throws JWaveException {
		this.evaluateDatagram(datagram);
	}

	
	
}
