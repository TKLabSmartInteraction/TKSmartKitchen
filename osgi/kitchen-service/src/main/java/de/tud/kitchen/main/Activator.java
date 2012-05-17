package de.tud.kitchen.main;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.tud.kitchen.api.Kitchen;

public class Activator implements BundleActivator {
	
	ServiceRegistration kitchenRegistration;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Kitchen Service Bundle");
		Kitchen kitchen = new KitchenService(context);
		kitchenRegistration = context.registerService(Kitchen.class.getName(), kitchen, null);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");
		kitchenRegistration.unregister();
	}
}