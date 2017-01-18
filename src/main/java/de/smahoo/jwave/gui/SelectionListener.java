package de.smahoo.jwave.gui;

import de.smahoo.jwave.cmd.JWaveCommand;
import de.smahoo.jwave.cmd.JWaveCommandClass;
import de.smahoo.jwave.node.JWaveNode;

public interface SelectionListener {
	public void onCommandClassSelected(JWaveCommandClass cmdClass);
	public void onCommandSelected(JWaveCommand cmd);
	public void onNodeSelected(JWaveNode node);
}
