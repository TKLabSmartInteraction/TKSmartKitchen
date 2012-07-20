package de.tud.kitchen.newArduino;



import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.furniture.DoorEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.arduino.Arduino;
import de.tud.kitchen.arduino.DoorSensorEvent;
import de.tud.kitchen.arduino.SensorEventListener;

/**
 * the Activator for the Arduino-board thats used to send
 * door activities
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator {
	//get an instance of the board
	Arduino ard = Arduino.getInstance();
	//and create an eventPublisher
	EventPublisher<DoorEvent> eventpublisher;

	@Override
	public void start(Kitchen kitchen) {	
		// assign the eventpublisher
		 eventpublisher = kitchen.getEventPublisher(DoorEvent.class);
		 // connect to the board and set it in the correct state
		 ard.connect("COM4");
		 ard.switchMode(Arduino.MCHANGES);
		 /* create and add a sensorEventListener thats called from the 
		  Arduino class and that publishes the DoorEvent to a kitchen
		  compatible one.
		  */
		 ard.addSensorEventListener(new SensorEventListener() {
			
			@Override
			public void SensorEvent(DoorSensorEvent arg0) {
				DoorEvent event = new DoorEvent(arg0.getSensor(), arg0.getValue());
				eventpublisher.publish(event);			
			}
		});
		
		
	}

	//when the bundle is stopped disconnect the Board
	@Override
	public void stop() {
		ard.disconnect();
		
	}
}