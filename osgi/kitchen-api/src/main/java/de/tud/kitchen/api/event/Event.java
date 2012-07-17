package de.tud.kitchen.api.event;

import java.io.ObjectInputStream.GetField;
import java.util.Locale;


public abstract class Event {

	public final String sender;
	public final long timestamp;
	
	public Event(String sender, long timestamp) {
		this.sender = sender;
		this.timestamp = timestamp;
	}
	
	public Event(String sender) {
		this(sender, System.currentTimeMillis());
	}
	
	public void dispatchTo(EventConsumer handler) {
		handler.handle(this);
	}
	
	@Override
	public String toString() {
		return String.format("%s sender: %s, time: %d",getClass().getSimpleName(), sender, timestamp);
	}

	public String logHeader() {
		return String.format("sender, time%s", getAdditionalHeader());
	}
	
	public String log() {
		return String.format(Locale.US, "%s, %d%s", sender, timestamp, getAdditionalLog());
	}
	
	protected abstract String getAdditionalLog();
	protected abstract String getAdditionalHeader();
}
