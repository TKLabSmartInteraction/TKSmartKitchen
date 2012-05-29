package de.tud.kitchen.api.event.acc;

import de.tud.kitchen.api.event.EventConsumer;


public abstract class AccelerometerEventConsumer<PRECISION> extends EventConsumer {

	@Override
	public void handle(Object o) {
		try {
			getMethod(o.getClass()).invoke(this, new Object[] { o });
		} catch (Exception ex) {
			System.out.println("no appropriate handle() method");
		}
	}

	public abstract void handleAccelerometerEvent(AccelerometerEvent<PRECISION> event);

}
