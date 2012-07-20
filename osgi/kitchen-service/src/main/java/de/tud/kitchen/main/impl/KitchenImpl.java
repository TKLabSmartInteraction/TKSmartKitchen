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

package de.tud.kitchen.main.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArraySet;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.main.impl.events.DispatchManager;

public class KitchenImpl implements KitchenInternal, Kitchen {

	private final HashMap<Class<?>, CopyOnWriteArraySet<EventConsumer>> eventsToConsumers;
	private final CopyOnWriteArraySet<EventConsumer> eventConsumers;
	private final DispatchManager dispatchManger;
	
	public KitchenImpl() {
		eventsToConsumers = new HashMap<Class<?>, CopyOnWriteArraySet<EventConsumer>>();
		eventConsumers = new CopyOnWriteArraySet<EventConsumer>();
		dispatchManger = new DispatchManager();
		dispatchManger.start();
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
	public <T extends Event> EventPublisher<T> getEventPublisher(final Class<T> eventType) {
		return new EventPublisher<T>() {
			@Override
			public void publish(T event) {
				try {
					dispatchManger.scheduleDispatch(eventsToConsumers.get(eventType), event);
				} catch (NullPointerException npe) {
					addEventClass(eventType);
					dispatchManger.scheduleDispatch(eventsToConsumers.get(eventType), event);
				}
			}
		};
	}
	
	public void stop() {
		synchronized (eventsToConsumers) {			
			for (EventConsumer consumer : new LinkedList<EventConsumer>(eventConsumers)) {
				removeEventConsumer(consumer);
			}
			dispatchManger.stop();
		}
	}
}
