/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.api.event.acc;

import java.util.Locale;

import de.tud.kitchen.api.event.Event;


/**
 * A basic {@link Event} for publishing accelerometer data.
 * 
 * @param <PRECISION> use one of the typical number types (Integer, Float or Double)
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public class AccelerometerEvent<PRECISION> extends Event {

	public final PRECISION x;
	public final PRECISION y;
	public final PRECISION z;
	
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
	protected String getAdditionalCsvHeader() {
		return ", x, y, z";
	}
	
	@Override
	protected String getAdditionalCsvValues() {
		return String.format(Locale.US, ", %15.12f, %15.12f, %15.12f",x,y,z);
	}

}
