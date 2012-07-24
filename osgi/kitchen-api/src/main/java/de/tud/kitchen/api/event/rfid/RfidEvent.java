/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Ständer <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.api.event.rfid;

import de.tud.kitchen.api.event.Event;

/**
 * 
 * @author Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 * 
 */
public class RfidEvent extends Event {
	public int rfid_reader_id;
	public boolean rfid_tag_gained;
	public String rfid_tag_value;

	/**
	 * RFID Event constructor. Stores the passed values.
	 * 
	 * @param sensor
	 *            ID of the RFID sensor which triggered the event.
	 * @param gained
	 *            True if the tag was gained, false if it was lost.
	 * @param value
	 *            The value of the rfid tag.
	 */
	public RfidEvent(int sensor, boolean gained, String value) {
		super("RFID Reader " + sensor);

		this.rfid_reader_id = sensor;
		this.rfid_tag_gained = gained;
		this.rfid_tag_value = value;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s", super.toString(), (rfid_tag_gained ? "gained" : "lost"), rfid_tag_value);
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return ", tag_gained, tag_id";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format(", %s, %s", (rfid_tag_gained ? "gained" : "lost"), rfid_tag_value);
	}

}
