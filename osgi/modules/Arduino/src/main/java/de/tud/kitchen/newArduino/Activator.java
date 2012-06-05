package de.tud.kitchen.newArduino;



import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.arduino.Arduino;
import de.tud.kitchen.arduino.DoorSensorEvent;
import de.tud.kitchen.arduino.SensorEventListener;


public class Activator extends KitchenModuleActivator {
	Arduino ard = Arduino.getInstance();
	EventPublisher<DoorEvent> eventpublisher;

	@Override
	public void start(Kitchen kitchen) {		
		 eventpublisher = kitchen.getEventPublisher(DoorEvent.class);
		 ard.connect();
		 ard.switchMode(Arduino.MCHANGES);
		 ard.addSensorEventListener(new SensorEventListener() {
			
			@Override
			public void SensorEvent(DoorSensorEvent arg0) {
				DoorEvent event = new DoorEvent(arg0.getSensor(), arg0.getValue());
				eventpublisher.publish(event);
				
			}
		});
		
		
	}
	public class DoorEventConsumer extends EventConsumer {
		public void handleDoorEvent(DoorEvent event) {
			
		}
			
	
	}

	@Override
	public void stop() {
		ard.disconnect();
		
	}
}