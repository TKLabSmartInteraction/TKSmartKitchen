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

package de.tud.kitchen.api;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

/**
 * This is the interface to which the kitchen service provides the
 * implementation.</br> A bundle which has an Activator that extends
 * KitchenModuleActivator gets an object with this interface injected through
 * the start method.
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public interface Kitchen {

	/**
	 * get an {@link EventPublisher} for a specific event type from the Kitchen
	 * implementation
	 * 
	 * @param eventType
	 * @return the EventPublisher
	 */
	<T extends Event> EventPublisher<T> getEventPublisher(Class<T> eventType);

	/**
	 * register an {@link EventConsumer}. </br> the consumer can be unregistered
	 * manually with {@link #removeEventConsumer(EventConsumer)} or will be
	 * unregistered automatically when the KitchenModule that uses this Kitchen
	 * instance is stopped
	 * 
	 * @param consumer to be registered
	 */
	void registerEventConsumer(EventConsumer consumer);

	/**
	 * remove an EventConsumer manually, this is almost never necessary since
	 * EventConsumer get automatically unregistered once a module is stopped
	 * 
	 * @param consumer to be unregistered
	 */
	void removeEventConsumer(EventConsumer consumer);

}
