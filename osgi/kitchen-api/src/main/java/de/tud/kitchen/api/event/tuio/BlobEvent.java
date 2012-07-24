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

public class BlobEvent extends TUIOEvent {

	public final int touchedTangible;
	
	public BlobEvent(String senderId, long timestamp, Point3f position, Point4f velocity, float tableDistance, int tangibleID) {
		super(senderId, timestamp, position, velocity, tableDistance);
		this.touchedTangible = tangibleID;
	}
	
	@Override
	public String toString() {
		return String.format("%s, touchedTangible: %d", super.toString(), this.touchedTangible);
	}
	
	@Override
	protected String getAdditionalCsvHeader() {
		return super.getAdditionalCsvHeader() + ", touchedTangible";
	}
	
	@Override
	protected String getAdditionalCsvValues() {
		return String.format("%s, %d", super.getAdditionalCsvValues(), touchedTangible);
	}

}
