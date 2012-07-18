package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class HandEvent extends TUIOEvent {

	public final int handId;
	public final Point3f pointingTo;
	
	public HandEvent(String sender, long timestamp, Point3f position, Point4f velocity, int handId, float tableDistance, Point3f pointingTo) {
		super(sender, timestamp, position, velocity, tableDistance);
		this.pointingTo = pointingTo;
		this.handId = handId;
	}
	
	@Override
	public String toString() {
		return String.format("%s, handId: %d, tableDistance: %f.4, pointingTo: %s", super.toString(), handId, tableDistance, pointingTo);
	}
	
	@Override
	protected String getAdditionalHeader() {
		return super.getAdditionalHeader() + ", hand, pointingX, pointingY, pointingZ";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format("%s, %d, %15.12f, %15.12f, %15.12f", super.getAdditionalLog(), handId, pointingTo.x, pointingTo.y, pointingTo.z);
	}

}
