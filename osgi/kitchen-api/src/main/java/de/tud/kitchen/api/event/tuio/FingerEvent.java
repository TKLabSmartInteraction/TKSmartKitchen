package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import de.tud.kitchen.api.event.tuio.TUIOEvent;

public class FingerEvent extends TUIOEvent {

	public FingerEvent(String sender, long timestamp, Point3f pos,
			Point4f velocity) {
		super(sender, timestamp, pos, velocity);
	}

}
