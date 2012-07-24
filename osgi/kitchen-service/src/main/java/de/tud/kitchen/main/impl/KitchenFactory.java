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

package de.tud.kitchen.main.impl;

import java.util.HashMap;
import java.util.HashSet;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModule;

public class KitchenFactory implements IKitchenFactory {

	private HashMap<KitchenModule, KitchenInternal> sandboxedKitchens = new HashMap<KitchenModule, KitchenInternal>();
	private KitchenInternal kitchen = new KitchenImpl();
	
	@Override
	public KitchenInternal getSingletonKitchen() {
		return kitchen;
	}
	
	@Override
	public KitchenInternal getSandboxedKitchen(KitchenModule module) {
		if (sandboxedKitchens.containsKey(module)) {
			return sandboxedKitchens.get(module);
		} else {
			KitchenInternal newKitchen = new SandboxedKitchen();
			sandboxedKitchens.put(module, newKitchen);
			return newKitchen;
		}
	}
	
	class SandboxedKitchen implements KitchenInternal {
		private HashSet<EventConsumer> ownEventConsumers = new HashSet<EventConsumer>();

		@Override
		public <T extends Event> EventPublisher<T> getEventPublisher(Class<T> eventType) {
			return kitchen.getEventPublisher(eventType);
		}

		@Override
		public void registerEventConsumer(EventConsumer consumer) {
			ownEventConsumers.add(consumer);
			kitchen.registerEventConsumer(consumer);
		}

		@Override
		public void removeEventConsumer(EventConsumer consumer) {
			ownEventConsumers.remove(consumer);
			kitchen.removeEventConsumer(consumer);
		}

		@Override
		public void stop() {
			for (EventConsumer consumer : ownEventConsumers) {
				kitchen.removeEventConsumer(consumer);
			}
			ownEventConsumers.clear();
		}
	}
}
