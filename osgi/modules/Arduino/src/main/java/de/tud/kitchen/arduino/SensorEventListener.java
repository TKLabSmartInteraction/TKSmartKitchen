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
