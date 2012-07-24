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

import de.tud.kitchen.api.event.Event;

/**
 * Base class for all events in package de.tud.kitchen.api.event.tuio
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 *
 */
public abstract class TUIOEvent extends Event {

	/**
	 * position where the subject of this event is located
	 */
	public final Point3f position;
	
	/**
	 * direction into which the subject of this event is moving including speed as fourth value
	 */
	public final Point4f velocity;
	
	/**
	 * approximate distance of the subject to the tables surface
	 */
	public final float tableDistance;
	
	public TUIOEvent(String sender, long timestamp, Point3f pos, Point4f velocity, float tableDistance) {
		super(sender,timestamp);
		this.position = pos;
		this.velocity = velocity;
		this.tableDistance = tableDistance;
	}
	
	@Override
	public String toString() {
		return String.format("%s, position: %s, velocity: %s, tableDistance %f.4", super.toString(), position, velocity, tableDistance);
	}
	
	@Override
	protected String getAdditionalCsvHeader() {
		return ",positionX, positionY, positionZ, movementX, movementY, movementZ, velocity, tableDistance";
	}
	
	@Override
	protected String getAdditionalCsvValues() {
		return String.format(", %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f", position.x, position.y, position.z, velocity.x, velocity.y, velocity.z, velocity.w, tableDistance);
	}

}
