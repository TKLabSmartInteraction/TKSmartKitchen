package de.tud.kitchen.arduino;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;




/**
 * this class represents an sensor event
 * with the original Serial event
 * the sensor name represented by a lowercase char
 * and the sensor status
 * @author cklos
 *
 */
public class DoorSensorEvent extends SerialPortEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8042367384009053908L;
	private Character Sensor;
	private boolean Value;
	
	
	public DoorSensorEvent(SerialPortEvent sE) {
		super((SerialPort) sE.getSource(), 
				sE.getEventType(), 
				sE.getOldValue(), 
				sE.getNewValue());
	}
	
	
	public DoorSensorEvent(SerialPortEvent sE, Character Sensor, boolean Value){
		super((SerialPort) sE.getSource(), 
				sE.getEventType(), 
				sE.getOldValue(), 
				sE.getNewValue());
		this.Sensor = Sensor;
		this.Value = Value;
	}
	
	public Character getSensor() {
		return Sensor;
	}
	
	public boolean getValue() {
		return Value;
	}

}
