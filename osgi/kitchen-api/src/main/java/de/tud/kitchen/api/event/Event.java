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

package de.tud.kitchen.api.event;

import java.util.Locale;

/**
 * Base class for all events in the kitchen event system. </br> To introduce a
 * new event type, subclass {@link Event} and introduce your own fields. It is
 * strongly advised to make your own fields also immutable and public (public
 * final) in order to prevent race conditions or undesired effects due to
 * threading. </br> Override {@link #getAdditionalCsvHeader()} and
 * {@link #getAdditionalCsvValues()}. You should also overwrite
 * {@link #toString()}.
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public abstract class Event {

	/**
	 * A string identifying the sender of this event. The string can be
	 * anything, but a kind of path string is convenient. There are two
	 * conventions that can be followed:
	 * <ol>
	 * <li>Use an identifier per device (e.g sender = "/knives/1")</li>
	 * <li>Use an identifier per group of event originators and introduce an
	 * additional id field in your event subclass (e.g. sender = "/knives" and
	 * KniveEvent has an id field)</li>
	 * </ol>
	 * Which convention you follow is up to you, but when senders are
	 * short-lived option 2 is preferred (e.g. the finger recognizer has a
	 * counter that gives a new number to each detected finger).
	 */
	public final String sender;

	/**
	 * Timestamp in milliseconds since epoch. All event publishing components
	 * should try to bring events into sync with the computer time before
	 * publishing them to the system.
	 */
	public final long timestamp;

	/**
	 * Default constructor
	 * 
	 * @param sender
	 *            value for {@link #sender}
	 * @param timestamp
	 *            value for {@link #timestamp}
	 */
	public Event(String sender, long timestamp) {
		this.sender = sender;
		this.timestamp = timestamp;
	}

	/**
	 * Reduced constructor using {@link System#currentTimeMillis()} to fill the
	 * {@link #timestamp}
	 * 
	 * @param sender
	 */
	public Event(String sender) {
		this(sender, System.currentTimeMillis());
	}

	@Override
	public String toString() {
		return String.format("%s sender: %s, time: %d", getClass().getSimpleName(), sender, timestamp);
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
