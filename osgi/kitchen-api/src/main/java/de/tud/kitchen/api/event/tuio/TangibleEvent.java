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
 * Event that comes from the DSensingNI framework. This event occurs when
 * DSensingNI detects a tangible on the table which can be any object that was
 * not there at calibration time
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public class TangibleEvent extends TUIOEvent {

	/**
	 * volumetric dimensions of the tangible
	 */
	public final Point3f dimension;
	
	/**
	 * parent tangible if tangible are stacked or moved into each other. Otherwise -1
	 */
	public final Integer parentTangible;
	
	/**
	 * hand id which is grabbing this tangible. If the tangible is not grabbed -1.
	 */
	public final Integer grabbedByHand;

	public TangibleEvent(String sender, long timestamp, Point3f pos, Point4f velocity, float tableDistance,
			Point3f dimension, Integer parentTangible, Integer grabbedByHand) {
		super(sender, timestamp, pos, velocity, tableDistance);
		this.dimension = dimension;
		this.parentTangible = parentTangible;
		this.grabbedByHand = grabbedByHand;
	}

	@Override
	public String toString() {
		return String.format("%s, dimensions: %s, parentTangible: %s, hand: %s", super.toString(), dimension,
				parentTangible, grabbedByHand);
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return super.getAdditionalCsvHeader() + ", dimensionX, dimensionY, dimensionZ, parentTangible, hand";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format("%s, %15.12f, %15.12f, %15.12f, %d, %d", super.getAdditionalCsvValues(), dimension.x,
				dimension.y, dimension.z, parentTangible, grabbedByHand);
	}
}
