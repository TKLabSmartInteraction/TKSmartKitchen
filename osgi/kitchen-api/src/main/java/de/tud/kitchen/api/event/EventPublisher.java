package de.tud.kitchen.api.event;

public interface EventPublisher<T> {

	void publish(T event);
}
