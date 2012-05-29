package de.tud.kitchen.api.event;


public abstract class Event {

	public void dispatchTo(EventConsumer handler) {
		handler.handle(this);
	}
}
