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

package de.tud.kitchen.api.event;

import java.io.ObjectInputStream.GetField;
import java.util.Locale;


public abstract class Event {

	public final String sender;
	public final long timestamp;
	
	public Event(String sender, long timestamp) {
		this.sender = sender;
		this.timestamp = timestamp;
	}
	
	public Event(String sender) {
		this(sender, System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return String.format("%s sender: %s, time: %d",getClass().getSimpleName(), sender, timestamp);
	}

	/**
	 * Create a CSV logfile header. This method can not be overridden to ensure
	 * that sender and time is always included in the log. Override
	 * {@link #getAdditionalCsvHeader()} to add your own fields to the header.
	 * 
	 * @return header for a CSV log
	 */
	public final String csvHeader() {
		return String.format("sender, time%s", getAdditionalCsvHeader());
	}

	/**
	 * Generate a CSV logfile entry This method can not be overridden to ensure
	 * that sender and time is always included in the entry. Override
	 * {@link #getAdditionalCsvValues()} to add the values of your own fields.
	 * 
	 * @return entry to be written to a CSV log file
	 */
	public final String csvValues() {
		return String.format(Locale.US, "%s, %d%s", sender, timestamp, getAdditionalCsvValues());
	}

	/**
	 * Generate a string that gets appened to the default header from
	 * {@link #csvHeader()}. When overridden, this method must not return null
	 * but an empty string in case nothing has to be appended to the default
	 * header. Otherwise the string returned by this function must start with
	 * ", " to ensure correct formating of the CSV header.
	 * 
	 * @return string appended to the {@link #csvHeader()} (e.g. ", x, y, z")
	 */
	protected abstract String getAdditionalCsvHeader();
	
	
	/**
	 * Generate a string that gets appended to the default values from
	 * {@link #csvValues()}. When overridden, this method must not return null
	 * but an empty string in case nothing has to be appended to the default
	 * values. Otherwise the string returned by this function must start with
	 * ", " to ensure correct formating of the CSV entry. Also make sure that
	 * the sequence of values matches your header from
	 * {@link #getAdditionalCsvHeader()}.
	 * 
	 * @return string appended to the {@link #csvValues()} (e.g. ", 0.23132, 0.12333, 0.9089343")
	 */
	protected abstract String getAdditionalCsvValues();

}
