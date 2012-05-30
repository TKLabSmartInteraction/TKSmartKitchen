package de.tud.kitchen.api.event;


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
}
