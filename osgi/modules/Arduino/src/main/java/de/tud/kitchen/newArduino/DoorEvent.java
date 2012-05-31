package de.tud.kitchen.newArduino;

import de.tud.kitchen.api.event.Event;

public class DoorEvent extends Event{
	
	private Character Sensor;
	private boolean Value;

	public DoorEvent(String sender) {
		super(sender);
	}
	
	public DoorEvent(Character sensor, boolean value){
		super("Door "+sensor);
		this.Sensor = sensor;
		this.Value = value;
	}
	

	public Character getSensor() {
		return Sensor;
	}
	
	public boolean getValue() {
		return Value;
	}
	
}
