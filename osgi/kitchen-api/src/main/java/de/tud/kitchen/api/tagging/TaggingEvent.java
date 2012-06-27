package de.tud.kitchen.api.tagging;

import de.tud.kitchen.api.event.Event;

public class TaggingEvent extends Event{
	
	String msg = "";

	public TaggingEvent(String sender, String msg) {
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
