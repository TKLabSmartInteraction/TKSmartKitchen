package de.tud.kitchen.api.event.acc;

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
	public long timestamp;
	public String id;
	
	public AccelerometerEvent(long timestamp, String id, PRECISION x, PRECISION y, PRECISION z) {
		this.timestamp = timestamp;
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof AccelerometerEvent<?>) {
			AccelerometerEvent<?> other = (AccelerometerEvent<?>) obj;
			if (other.timestamp != this.timestamp) return false;
			if (other.id != this.id) return false;
			if (other.x.equals(this.x) && other.y.equals(this.y) && other.z.equals(this.z))
				return true;
		}
		return false;
	}

}
