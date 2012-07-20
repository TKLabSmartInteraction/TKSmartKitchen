package arduino;

public interface SensorEventListener {
	
	/**
	 * this method is called when sensor data arrives
	 * @param arg0 the sensor data that arrives
	 */
	public void SensorEvent(SensorEvent arg0);

}
