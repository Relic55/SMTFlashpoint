package vialab.SMT.event;

import java.util.EventObject;

import vialab.SMT.ButtonZone;

public class ButtonEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ButtonZone source;
	private String buttonCommand;
	
	public ButtonEvent(ButtonZone source, String command) {
		super(source);
		this.source = source;
		this.buttonCommand = command;
	}
	
	public ButtonZone getSource() {
		return this.source;
	}
	
	public String getActionCommand() {
		return this.buttonCommand;
	}
	
}