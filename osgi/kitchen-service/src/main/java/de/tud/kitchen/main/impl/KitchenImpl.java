package de.tud.kitchen.main.impl;

import java.util.HashSet;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

public class KitchenImpl implements Kitchen {

	private HashSet<EventConsumer> eventConsumers;
	
	public KitchenImpl() {
		eventConsumers = new HashSet<EventConsumer>();
	}
	
	@Override
	public void registerEventConsumer(EventConsumer consumer) {
		eventConsumers.add(consumer);
	}

	@Override
	public <T> EventPublisher<T> getEventPublisher(Class<T> eventType) {
		return new EventPublisher<T>() {
			@Override
			public void publish(T event) {
				for (EventConsumer consumer : eventConsumers) {
					consumer.handle(event);
				}
			}
		};
	}

}
