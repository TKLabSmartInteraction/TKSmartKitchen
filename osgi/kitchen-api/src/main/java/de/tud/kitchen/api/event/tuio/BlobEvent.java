package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class BlobEvent extends TUIOEvent {

	public final int touchedTangible;
	
	public BlobEvent(String senderId, long timestamp, Point3f position, Point4f velocity, float tableDistance, int tangibleID) {
		super(senderId, timestamp, position, velocity, tableDistance);
		this.touchedTangible = tangibleID;
	}
	
	@Override
	public String toString() {
		return String.format("%s, touchedTangible: %d", super.toString(), this.touchedTangible);
	}

}
