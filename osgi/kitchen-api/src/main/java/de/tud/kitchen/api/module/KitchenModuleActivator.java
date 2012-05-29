package de.tud.kitchen.api.module;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.tud.kitchen.api.Kitchen;

public abstract class KitchenModuleActivator implements KitchenModule, BundleActivator {

	
	private ServiceRegistration registration;
	
	@Override
	public void start(BundleContext context) throws Exception {
		registration = context.registerService(KitchenModule.class.getName(), this, null);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
	}
	
	abstract public void start(Kitchen kitchen);
	abstract public void stop();
	
}
