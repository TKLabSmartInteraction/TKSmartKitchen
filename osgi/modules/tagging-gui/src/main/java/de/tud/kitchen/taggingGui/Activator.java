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

package de.tud.kitchen.taggingGui;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.api.tagging.TaggingEvent;


/**
 * 
 * @author Christian Klos
 * the Starter for the tagging Gui
 */
public class Activator extends KitchenModuleActivator {

	EventPublisher<TaggingEvent> publisher;
	gui mygui;
	
	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(TaggingEvent.class);
		mygui = new gui(publisher);
	}

	@Override
	public void stop() {
		mygui.close();		
	}
	
	
}