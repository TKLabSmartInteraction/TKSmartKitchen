package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class HandEvent extends TUIOEvent {

	public final Point3f pointingTo;
	
	public HandEvent(String sender, long timestamp, Point3f position, Point4f velocity, float tableDistance, Point3f pointingTo) {
		super(sender, timestamp, position, velocity, tableDistance);
		this.pointingTo = pointingTo;
	}
	
	@Override
	public String toString() {
		return String.format("%s, tableDistance: %f.4, pointingTo: %s", super.toString(), tableDistance, pointingTo);
	}

}
