package de.tud.kitchen.api.event.furniture;

import de.tud.kitchen.api.event.Event;

public class DoorEvent extends Event {
	
	public final Character sensor;
	public final boolean closed;
	
	public DoorEvent(Character sensor, boolean value){
		super("Door "+sensor);
		this.sensor = sensor;
		this.closed = value;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", super.toString(), (closed)?"closed":"open");
	}
	
}
