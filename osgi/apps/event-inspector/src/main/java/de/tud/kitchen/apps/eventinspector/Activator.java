package de.tud.kitchen.apps.eventinspector;

import java.awt.EventQueue;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	
	private EventWindow window;
	
	@Override
	public void start(final Kitchen kitchen) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new EventWindow();
					kitchen.registerEventConsumer(window.getEventConsumer());
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				window.getFrame().setVisible(false);
			}
		});
	}
}