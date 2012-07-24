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

package de.tud.kitchen.api.event.tuio;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import de.tud.kitchen.api.event.tuio.TUIOEvent;

public class FingerEvent extends TUIOEvent {

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
