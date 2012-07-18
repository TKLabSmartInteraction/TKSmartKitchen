package de.tud.kitchen.taggingGui;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.api.tagging.TaggingEvent;


/**
 * 
 * @author Christian Klos
 * the Starter for the tagging Gui
 */
public class Activator extends KitchenModuleActivator {

	EventPublisher<TaggingEvent> publisher;
	gui mygui;
	
	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(TaggingEvent.class);
		mygui = new gui(publisher);
	}

	@Override
	public void stop() {
		mygui.close();		
	}
	
	
}