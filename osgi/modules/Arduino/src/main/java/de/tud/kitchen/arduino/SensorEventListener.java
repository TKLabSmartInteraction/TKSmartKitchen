package de.tud.kitchen.arduino;

/**
 * Should be implemented by everyone that wants to be notified
 * about DoorSensor changes
 * @author Christian Klos
 *
 */
public interface SensorEventListener {
	
	/**
	 * this method is called when sensor data arrives
	 * @param arg0 the sensor data that arrives
	 */
	public void SensorEvent(DoorSensorEvent arg0);

}
