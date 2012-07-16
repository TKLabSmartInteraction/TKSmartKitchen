package de.tud.kitchen.api;

import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModule;

public interface Kitchen {

	/**
	 * get an EventPublisher for a specific event type from the Kitchen implementation
	 * 
	 * @param eventType
	 * @return the EventPublisher
	 */
	<T> EventPublisher<T> getEventPublisher(Class<T> eventType);
	
	/**
	 * register an EventConsumer. </br>
	 * the consumer can be unregistered manually and will be unregistered automatically when the KitchenModule is stopped
	 * 
	 * @param consumer
	 */
	void registerEventConsumer(EventConsumer consumer);

	
	/**
	 * remove an EventConsumer manually
	 * 
	 * @param consumer
	 */
	void removeEventConsumer(EventConsumer consumer);

}
