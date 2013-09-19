package de.tud.kitchen.wax;

public class WAXSample {

	public WAXSample(AccerlerationVector acceleration, long timestamp){
		this.acceleration = acceleration;
		this.timestamp = timestamp;
	}
	
	private AccerlerationVector acceleration;
	private long timestamp;
	
	public AccerlerationVector getAcceleration(){
		return this.acceleration;
	}
	
	public long getTimestamp(){
		return this.timestamp;
	}
}
