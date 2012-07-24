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

package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

/**
 * Event that comes from the DSensingNI framework. A HandEvent occurs when the
 * DSensingNI framework detects a hand over the table. This is also the case
 * when the user makes a fist.
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public class HandEvent extends TUIOEvent {

	/**
	 * ID of this hand
	 */
	public final int handId;
	
	/**
	 * direction in which this hand is pointed
	 */
	public final Point3f pointingTo;

	public HandEvent(String sender, long timestamp, Point3f position, Point4f velocity, int handId,
			float tableDistance, Point3f pointingTo) {
		super(sender, timestamp, position, velocity, tableDistance);
		this.pointingTo = pointingTo;
		this.handId = handId;
	}

	@Override
	public String toString() {
		return String.format("%s, handId: %d, tableDistance: %f.4, pointingTo: %s", super.toString(), handId,
				tableDistance, pointingTo);
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return super.getAdditionalCsvHeader() + ", hand, pointingX, pointingY, pointingZ";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format("%s, %d, %15.12f, %15.12f, %15.12f", super.getAdditionalCsvValues(), handId, pointingTo.x,
				pointingTo.y, pointingTo.z);
	}

}
