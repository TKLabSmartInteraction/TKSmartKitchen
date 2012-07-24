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

import de.tud.kitchen.api.event.tuio.TUIOEvent;

/**
 * Event that comes from the DSensingNI framework. Finger events occurs when
 * DSensingNI detects fingers, which is usually when the user holds his hand
 * flat over the table or extends a number of fingers.
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 * 
 */
public class FingerEvent extends TUIOEvent {

	/**
	 * The hand to which this finger belongs
	 */
	public final int handId;

	public FingerEvent(String sender, long timestamp, Point3f pos, Point4f velocity, float tableDistance, int handId) {
		super(sender, timestamp, pos, velocity, tableDistance);
		this.handId = handId;
	}

	@Override
	public String toString() {
		return String.format("%s, handId: %d", super.toString(), this.handId);
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return super.getAdditionalCsvHeader() + ", hand";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format("%s, %d", super.getAdditionalCsvValues(), handId);
	}

}
