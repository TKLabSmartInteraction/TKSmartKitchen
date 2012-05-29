package de.tud.kitchen.api;

import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

public interface Kitchen {

	/**
	 * get an EventPublisher for a specific event type from the Kitchen implementation
	 * 
	 * @param eventType
	 * @return the EventPublisher
	 */
	<T> EventPublisher<T> getEventPublisher(Class<T> eventType);
	
	/**
	 * register an EventConsumer for a specific event type
	 * the consumer will be unregistered automatically when the KitchenModule
	 * 
	 * @param consumer
	 */
	void registerEventConsumer(EventConsumer consumer);
}
