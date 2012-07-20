package de.tud.kitchen.blenderReceiver;

import org.mundo.rt.Mundo;
import org.osgi.framework.BundleContext;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModuleActivator;


public class Activator extends KitchenModuleActivator {
	
	public void start(BundleContext context) throws Exception {
		
		
	}

	

	@Override
	public void start(Kitchen kitchen) {
		Mundo.init();
        BlenderReciever blenderReciever = new BlenderReciever(kitchen.getEventPublisher(BlenderEvent.class));
        Mundo.registerService(blenderReciever);		
	}

	@Override
	public void stop() {
		Mundo.shutdown();
		
	}
}