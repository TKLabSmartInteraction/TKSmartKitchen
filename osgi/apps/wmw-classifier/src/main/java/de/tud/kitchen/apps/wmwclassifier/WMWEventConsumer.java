package de.tud.kitchen.apps.wmwclassifier;

import javax.swing.SwingUtilities;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;

/**
 * EventConsumer to receive all events regardless of the Event-class
 * filtering happens in {@link DynamicEventFilter}
 * */
public class WMWEventConsumer extends EventConsumer {


	public WMWEventConsumer() {
		// Whatever
	}

	public void handleEvent(final Event event) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				System.out.println("Received event of type: " + event.getClass() + " -> " + event);
			}
		});
	}
}
	