package de.tud.kitchen.phidgets;

import java.util.Date;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	EventPublisher<AccelerometerEvent> publisher;

	private RFIDPhidget rfid;

	@Override
	public void start(Kitchen kitchen) {
		System.out.println("Phidget bundle started");
		publisher = kitchen.getEventPublisher(AccelerometerEvent.class);
	}

	@Override
	public void stop() {

	}

//	private class parametricOscListener implements OSCListener {
//		String source;
//		private long lastTime;
//
//		public parametricOscListener(String source) {
//			super();
//			this.source = source;
//		}
//
//		@Override
//		public void acceptMessage(Date arg0, OSCMessage arg1) {
//			long time = ((Date) arg1.getArguments()[4]).getTime();
//			if (time == 0) 
//				time = lastTime+20;
//			lastTime=time;
//			final AccelerometerEvent<Float> event = new AccelerometerEvent<Float>(source,
//					time, 
//					(Float) arg1.getArguments()[0], 
//					(Float) arg1.getArguments()[1],
//					(Float) arg1.getArguments()[2]);
//			publisher.publish(event);
//		}
//
//	}
}