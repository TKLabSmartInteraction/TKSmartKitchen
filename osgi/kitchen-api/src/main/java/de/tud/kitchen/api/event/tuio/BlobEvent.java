package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class BlobEvent extends TUIOEvent {

	public BlobEvent(String senderId, long timestamp, Point3f position, Point4f velocity) {
		super(senderId, timestamp, position, velocity);
	}

}
