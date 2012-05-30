package de.tud.kitchen.api.module;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import de.tud.kitchen.api.Kitchen;


/**
 * Convenient base class for Activators used in modules for the SmartKitchen environment
 * 
 * 
 * @author niklas
 */
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
	
	/**
	 * Gets called when the kitchen-service bundle detects and loads the module.</br>
	 * In this method you will typically get the EventPublisher and register EventConsumers used by this module
	 * <pre>
	 * kitchen.getEventPublisher(MyCustomEvent.class)
	 * kitchen.registerEventConsumer(myConsumer);
	 * </pre>
	 */
	abstract public void start(Kitchen kitchen);
	
	/**
	 * Gets called when the kitchen-service bundle decides to unload the module
	 */
	abstract public void stop();
	
}
