package de.smahoo.jwave.gui;

import javax.swing.JPanel;

import java.awt.CardLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.smahoo.jwave.cmd.*;

import java.awt.Font;
import java.util.List;

@SuppressWarnings("serial")
public class PanelZWaveParamDetails extends JPanel{

	private JWaveCommand cmd = null;
	private JWaveCommandClass cmdClass = null;
	
	public PanelZWaveParamDetails(){
		
		initGui();
		update();
	}
	
	public void setParam(JWaveCommandClass cmdClass, JWaveCommand cmd){

		this.cmd = cmd;
		this.cmdClass = cmdClass;
		update();
	}
	
	
	private void update(){
		txtDescription.setText("");
		if (cmd == null) return;
		
		String txt = "";
		txt += "\r\n   Command Class : "+cmdClass.getName();
		txt += "\r\n             key : "+getHex(cmdClass.getKey());
		txt += "\r\n         version : "+cmdClass.getVersion();
		txt += "\r\n     description : "+cmdClass.getHelp();
		txt += "\r\n";
		
		txt += "\r\n         Command : "+cmd.getName();
		txt += "\r\n             key : "+getHex(cmd.getKey());
		txt += "\r\n     description : "+cmd.getHelp();
		txt += "\r\n";
		
		
		String space =  "                ";
		List<JWaveCommandParameter> paramList = cmd.getParamList();
		for (JWaveCommandParameter param : paramList){
			txt =addParamDetails(param,space,txt);		
			
			for (JWaveCommandParameterValue value : param.getValues()){
				txt += generateValueText(value,space+"        ");
			}
			
				
		}
			
		
		
		txtDescription.setText(txt);
	}
	
	private String addParamDetails(JWaveCommandParameter param, String space, String txt){
		txt += "\r\n"+space+"parameter : "+param.getKey();	
		txt += "\r\n"+space+"name : "+param.getName();
		txt += "\r\n"+space+"type : "+param.getType().name();		
		txt += "\r\n";	
		if (param instanceof JWaveCommandParameterVariantGroup){
			for (JWaveCommandParameter supParam : ((JWaveCommandParameterVariantGroup)param).getParams()){
				txt = addParamDetails(supParam, space+"     ", txt);	
			}
		}		
		return txt;
	}
	
	private String generateValueText(JWaveCommandParameterValue value, String space){
		String res = "";
		if (value instanceof JWaveCommandParameterBitflag){
			JWaveCommandParameterBitflag bitflag = (JWaveCommandParameterBitflag)value;
			res += "\r\n"+space+"BitfFlag : "+getHex(bitflag.getFlagMask())+" "+bitflag.getFlagName();			
			
		}
		if (value instanceof JWaveCommandParameterBitfield){
			JWaveCommandParameterBitfield bitField = (JWaveCommandParameterBitfield)value;
			res += "\r\n"+space+"BitField : "+getHex(bitField.getFieldMask())+" "+ bitField.getFieldName() + " shift="+bitField.getShifter();
		}
		if (value instanceof JWaveCommandParameterBitmask){
			JWaveCommandParameterBitmask mask = (JWaveCommandParameterBitmask)value;
			res += "\r\n"+space+" BitMask :  len = "+getHex(mask.getLenMask())+ " | lenOffs = "+mask.getLenOffs()+" | paramOffs = "+mask.getParamOffs();
		}
		//if (value instanceof ZWaveCmdParamAttribute){
		//	ZWaveCmdParamAttribute attr = (ZWaveCmdParamAttribute)value;			
	//		res += "\r\n                             Attribute : "; 
	//	}
		if (value instanceof JWaveCommandParameterConstant){
			JWaveCommandParameterConstant c = (JWaveCommandParameterConstant)value;
			res += "\r\n                                 "+getHex(c.getFlagMask())+" "+c.getFlagName();
		}
		return res;
	}
	
	private String getHex(int value){
		String res = Integer.toHexString(value);
		if (res.length() < 2){
			res = "0"+res;
		}
		return "0x"+res;
	}
	
	
	private void initGui(){
		setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		
		add(scrollPane, "name_33834483066571");
		
		txtDescription = new JTextArea();
		txtDescription.setFont(new Font("Courier New", Font.PLAIN, 13));
		
		scrollPane.setViewportView(txtDescription);
	}
	
	private JTextArea txtDescription;
	
}
