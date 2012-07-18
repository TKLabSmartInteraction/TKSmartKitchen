package de.tud.kitchen.api.event.acc;

import java.util.Locale;

import de.tud.kitchen.api.event.Event;


/**
 * 
 * @author niklas
 * @param <PRECISION> use one of the typical number types (Integer, Float or Double)
 */
public class AccelerometerEvent<PRECISION> extends Event {

	public PRECISION x;
	public PRECISION y;
	public PRECISION z;
	
	public AccelerometerEvent(String sender, long timestamp, PRECISION x, PRECISION y, PRECISION z) {
		super(sender,timestamp);
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "%s, x: %15.12f, y: %15.12f, z: %15.12f", super.toString(), x, y, z);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof AccelerometerEvent<?>) {
			AccelerometerEvent<?> other = (AccelerometerEvent<?>) obj;
			if (other.timestamp != this.timestamp) return false;
			if (other.sender != this.sender) return false;
			if (other.x.equals(this.x) && other.y.equals(this.y) && other.z.equals(this.z))
				return true;
		}
		return false;
	}
	
	@Override
	protected String getAdditionalHeader() {
		return ", x, y, z";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format(Locale.US, ", %15.12f, %15.12f, %15.12f",x,y,z);
	}

}
