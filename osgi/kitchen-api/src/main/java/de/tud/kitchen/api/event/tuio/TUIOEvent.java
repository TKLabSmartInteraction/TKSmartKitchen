package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import de.tud.kitchen.api.event.Event;

public abstract class TUIOEvent extends Event {

	public final Point3f position;
	public final Point4f velocity;
	
	public TUIOEvent(String sender, long timestamp, Point3f pos, Point4f velocity) {
		super(sender,timestamp);
		this.position = pos;
		this.velocity = velocity;
	}
	
	@Override
	public String toString() {
		return String.format("%s, position: %s, velocity: %s", super.toString(), position, velocity);
	}

}
