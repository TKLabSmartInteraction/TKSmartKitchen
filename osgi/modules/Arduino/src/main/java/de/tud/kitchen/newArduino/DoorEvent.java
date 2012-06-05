package de.tud.kitchen.newArduino;

import de.tud.kitchen.api.event.Event;

public class DoorEvent extends Event{
	
	public final Character Sensor;
	public final boolean closed;
	
	public DoorEvent(Character sensor, boolean value){
		super("Door "+sensor);
		this.Sensor = sensor;
		this.closed = value;
	}

	@Override
	public String toString() {
		return String.format("DoorEvent door%s %s", Character.toUpperCase(Sensor), (closed)?"closed":"open");
	}
	
}
