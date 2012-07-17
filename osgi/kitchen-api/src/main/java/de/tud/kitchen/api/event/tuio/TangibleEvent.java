package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class TangibleEvent extends TUIOEvent {

	public final Point3f dimension;
	public final Integer parentTangible;
	public final Integer grabbedByHand;
	
	public TangibleEvent(String sender, long timestamp, Point3f pos, Point4f velocity, float tableDistance, Point3f dimension, Integer parentTangible, Integer grabbedByHand) {
		super(sender, timestamp, pos, velocity, tableDistance);
		this.dimension = dimension;
		this.parentTangible = parentTangible;
		this.grabbedByHand = grabbedByHand;
	}
	
	@Override
	public String toString() {
		return String.format("%s, dimensions: %s, parentTangible: %s, hand: %s",super.toString(),dimension,parentTangible,grabbedByHand);
	}
	
	@Override
	protected String getAdditionalHeader() {
		return super.getAdditionalHeader() + ", dimensionX, dimensionY, dimensionZ, parentTangible, hand";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format("%s, %15.12f, %15.12f, %15.12f, %d, %d", super.getAdditionalLog(), dimension.x, dimension.y, dimension.z, parentTangible, grabbedByHand);
	}
}
