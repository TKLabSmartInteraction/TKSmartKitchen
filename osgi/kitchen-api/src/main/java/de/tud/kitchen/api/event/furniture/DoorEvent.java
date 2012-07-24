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

package de.tud.kitchen.api.event.furniture;

import de.tud.kitchen.api.event.Event;

public class DoorEvent extends Event {
	
	/**
	 * the door that produced this event
	 */
	public final Character sensor;
	
	/**
	 * true = door closed
	 * false = door open
	 */
	public final boolean closed;
	
	public DoorEvent(Character sensor, boolean value){
		super("Door "+sensor);
		this.sensor = sensor;
		this.closed = value;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", super.toString(), (closed)?"closed":"open");
	}
	
	@Override
	protected String getAdditionalCsvHeader() {
		return ", closed";
	}
	
	@Override
	protected String getAdditionalCsvValues() {
		return String.format(", %s", ((closed)?"true":"false"));
	}
	
}
