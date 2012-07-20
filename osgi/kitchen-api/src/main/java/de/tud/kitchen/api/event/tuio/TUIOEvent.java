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

import de.tud.kitchen.api.event.Event;

public abstract class TUIOEvent extends Event {

	public final Point3f position;
	public final Point4f velocity;
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
	protected String getAdditionalHeader() {
		return ",positionX, positionY, positionZ, movementX, movementY, movementZ, velocity, tableDistance";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format(", %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f, %15.12f", position.x, position.y, position.z, velocity.x, velocity.y, velocity.z, velocity.w, tableDistance);
	}

}
