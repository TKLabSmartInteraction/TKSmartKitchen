package de.tud.kitchen.api.event.acc;

import de.tud.kitchen.api.event.EventConsumer;


public abstract class AccelerometerEventConsumer<PRECISION> extends EventConsumer {

	public abstract void handleAccelerometerEvent(AccelerometerEvent<PRECISION> event);

}
