package de.tud.kitchen.taggingGui;

import de.tud.kitchen.api.event.Event;

public class taggingEvent extends Event{
	
	String msg = "";

	public taggingEvent(String sender, String msg) {
		super(sender);
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", message: "+msg;
	}

}
