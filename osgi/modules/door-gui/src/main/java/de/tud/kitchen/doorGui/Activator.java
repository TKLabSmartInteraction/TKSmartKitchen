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

package de.tud.kitchen.doorGui;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.furniture.DoorEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

/**
 * the Activator for the DoorGui
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator {

	Gui doorGui;
	
	
	@Override
	public void start(Kitchen kitchen) {
		kitchen.registerEventConsumer(new myEventConsumer());
		doorGui = new Gui();
	}

	@Override
	public void stop() {
		doorGui = null;		
	}
	
	/**
	 * 
	 * @author Christian Klos
	 * Handles DoorEvents and sends them to the GUI 
	 */
	public class myEventConsumer extends EventConsumer {		
		public void handleDoorEvent (DoorEvent e){
			doorGui.updateGui(e);
		}
		
	}
}