package de.tud.kitchen.main.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

public class KitchenImpl implements KitchenInternal, Kitchen {

	private final HashMap<Class<?>, CopyOnWriteArraySet<EventConsumer>> eventsToConsumers;
	private final CopyOnWriteArraySet<EventConsumer> eventConsumers;
	
	public KitchenImpl() {
		eventsToConsumers = new HashMap<Class<?>, CopyOnWriteArraySet<EventConsumer>>();
		eventConsumers = new CopyOnWriteArraySet<EventConsumer>();
	}
	
	@Override
	public void registerEventConsumer(EventConsumer consumer) {
		synchronized (eventsToConsumers) {
			for (Entry<Class<?>, CopyOnWriteArraySet<EventConsumer>> entry: eventsToConsumers.entrySet()) {
				if (consumer.handles(entry.getKey()))
					entry.getValue().add(consumer);
			}
			eventConsumers.add(consumer);
		}
	}
	
	@Override
	public void removeEventConsumer(EventConsumer consumer) {
		synchronized (eventsToConsumers) {			
			for (Entry<Class<?>, CopyOnWriteArraySet<EventConsumer>> entry: eventsToConsumers.entrySet()) {
				if (consumer.handles(entry.getKey()))
					entry.getValue().remove(consumer);
			}
			eventConsumers.remove(consumer);
		}
	}
	
	private void addEventClass(Class<?> eventClass) {
		synchronized (eventsToConsumers) {
			final LinkedList<EventConsumer> list = new LinkedList<EventConsumer>();
			for (EventConsumer consumer : eventConsumers) {
				if (consumer.handles(eventClass))
					list.add(consumer);
			}
			eventsToConsumers.put(eventClass, new CopyOnWriteArraySet<EventConsumer>(list));
		}
	}

	@Override
	public <T> EventPublisher<T> getEventPublisher(final Class<T> eventType) {
		return new EventPublisher<T>() {
			@Override
			public void publish(T event) {
				try {
					for (EventConsumer consumer : eventsToConsumers.get(eventType)) {
						consumer.handle(event);
					}
				} catch (NullPointerException npe) {
					addEventClass(eventType);
					for (EventConsumer consumer : eventsToConsumers.get(eventType)) {
						consumer.handle(event);
					}
				}
			}
		};
	}
	
	public void stop() {
		synchronized (eventsToConsumers) {			
			for (EventConsumer consumer : new LinkedList<EventConsumer>(eventConsumers)) {
				removeEventConsumer(consumer);
			}
		}
	}
}
