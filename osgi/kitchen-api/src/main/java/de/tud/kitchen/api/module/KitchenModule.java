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

package de.tud.kitchen.api.module;

import de.tud.kitchen.api.Kitchen;

/**
 * A tracking interface used for the OSGi service registration and discovery.
 * 
 * @author Niklas Lochschmidt <nlochschmidt@gmail.com>
 */
public interface KitchenModule {

	/**
	 * called once the kitchen service is started and loaded this module.
	 * 
	 * @param kitchen object to interact with the kitchen service
	 */
	public void start(Kitchen kitchen);
	
	/**
	 * called once the kitchen module gets unloaded.
	 */
	public void stop();
}
