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

package de.tud.kitchen.api;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

public interface Kitchen {

	/**
	 * get an EventPublisher for a specific event type from the Kitchen implementation
	 * 
	 * @param eventType
	 * @return the EventPublisher
	 */
	<T extends Event> EventPublisher<T> getEventPublisher(Class<T> eventType);
	
	/**
	 * register an EventConsumer. </br>
	 * the consumer can be unregistered manually and will be unregistered automatically when the KitchenModule is stopped
	 * 
	 * @param consumer
	 */
	void registerEventConsumer(EventConsumer consumer);

	
	/**
	 * remove an EventConsumer manually
	 * 
	 * @param consumer
	 */
	void removeEventConsumer(EventConsumer consumer);

}
