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

/**
 * Use this interface to publish events to the kitchen publish-subscribe event
 * system.
 * 
 * @param <T> the eventType that this EventPublisher can publish
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public interface EventPublisher<T> {
	void publish(T event);
}
