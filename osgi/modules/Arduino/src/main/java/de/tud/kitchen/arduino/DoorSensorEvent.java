/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.arduino;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;




/**
 * this class represents an sensor event
 * with the original Serial event
 * the sensor name represented by a lowercase char
 * and the sensor status
 * @author Christian Klos
 *
 */
public class DoorSensorEvent extends SerialPortEvent {

	
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
