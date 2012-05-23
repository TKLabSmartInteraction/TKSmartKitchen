package de.tud.kitchen.template;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.tud.kitchen.arduino.Arduino;


public class Activator implements BundleActivator {
	Arduino ard = Arduino.getInstance();
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle");
		ard.connect();
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");
		ard.disconnect();
	}
}