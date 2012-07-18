package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import de.tud.kitchen.api.event.tuio.TUIOEvent;

public class FingerEvent extends TUIOEvent {

	public final int handId;

	public FingerEvent(String sender, long timestamp, Point3f pos, Point4f velocity, float tableDistance, int handId) {
		super(sender, timestamp, pos, velocity, tableDistance);
		this.handId = handId;
	}
	
	@Override
	public String toString() {
		return String.format("%s, handId: %d", super.toString(), this.handId);
	}
	
	@Override
	protected String getAdditionalHeader() {
		return super.getAdditionalHeader() + ", hand";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format("%s, %d", super.getAdditionalLog(), handId);
	}

}
