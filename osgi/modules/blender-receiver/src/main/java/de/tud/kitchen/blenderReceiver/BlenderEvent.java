package de.tud.kitchen.blenderReceiver;

import de.tud.kitchen.api.event.Event;

public class BlenderEvent extends Event {
	
	boolean pushed;
	
	public BlenderEvent(String sender, boolean pushed) {		
		super(sender);
		this.pushed = pushed;
	}
	
	public BlenderEvent(String sender) {
		super(sender);
	}


	@Override
	public String toString() {
		return "Button "+this.sender+(pushed ? " pushed" : " released");
	}

	@Override
	protected String getAdditionalLog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAdditionalHeader() {
		return String.format(", %s", ((pushed)?"pushed":"pushed"));
	}

	
	

}
