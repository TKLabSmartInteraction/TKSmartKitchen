package de.tud.kitchen.main.impl.events;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;

public class DispatchCallable implements Callable<Void> {
	
	//0: init
	//1: prepared
	//2: running
	//3: ran
	private AtomicInteger state = new AtomicInteger(0);
	private EventConsumer consumer;
	private Event event;
	
	void setData(EventConsumer consumer, Event event) {
		if (state.compareAndSet(0, 1) || state.compareAndSet(3, 1)) {
			this.consumer = consumer;
			this.event = event;
		} else {
			throw new IllegalStateException("new data can only be set while the Callable is not prepared and did not ran");
		}
	}
	
	public Void call() throws Exception {
		try {
			event.dispatchTo(consumer);
		} finally {
			state.set(3);
		}
		return null;
	}
}
