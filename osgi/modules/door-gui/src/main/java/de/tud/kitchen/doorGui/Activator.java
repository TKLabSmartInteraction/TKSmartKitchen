package de.tud.kitchen.doorGui;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.furniture.DoorEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

/**
 * the Activator for the DoorGui
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator {

	Gui doorGui;
	
	
	@Override
	public void start(Kitchen kitchen) {
		kitchen.registerEventConsumer(new myEventConsumer());
		doorGui = new Gui();
	}

	@Override
	public void stop() {
		doorGui = null;		
	}
	
	/**
	 * 
	 * @author Christian Klos
	 * Handles DoorEvents and sends them to the GUI 
	 */
	public class myEventConsumer extends EventConsumer {		
		public void handleDoorEvent (DoorEvent e){
			doorGui.updateGui(e);
		}
		
	}
}