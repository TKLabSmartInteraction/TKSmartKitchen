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

package de.tud.kitchen.blenderReceiver;

import org.mundo.rt.Mundo;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModuleActivator;

/**
 * Just the Activator for the BlenderReceiver, it starts the modules that
 * are necessary to recieve and process BlenderEvents
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator {
	
	@Override
	public void start(Kitchen kitchen) {
		Mundo.init();
        BlenderReciever blenderReciever = new BlenderReciever(kitchen.getEventPublisher(BlenderEvent.class));
        Mundo.registerService(blenderReciever);		
	}

	@Override
	public void stop() {
		Mundo.shutdown();
		
	}
}