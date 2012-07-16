package de.tud.kitchen.main.impl;

import java.util.HashMap;
import java.util.HashSet;

import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModule;

public class KitchenFactory implements IKitchenFactory {

	private HashMap<KitchenModule, KitchenInternal> sandboxedKitchens = new HashMap<KitchenModule, KitchenInternal>();
	private KitchenInternal kitchen = new KitchenImpl();
	
	@Override
	public KitchenInternal getSingletonKitchen(KitchenModule module) {
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
		public <T> EventPublisher<T> getEventPublisher(Class<T> eventType) {
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
