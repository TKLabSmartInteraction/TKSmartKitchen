package de.tud.kitchen.main;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModule;

public class Activator implements BundleActivator {
	
	public ServiceTracker kitchenModuleTracker;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Kitchen Service Bundle");
		kitchenModuleTracker = new ServiceTracker(context, KitchenModule.class.getName(), new KitchenModuleTrackerCustomizer(context));
		kitchenModuleTracker.open();
	}

	public void stop(BundleContext context) throws Exception {
		kitchenModuleTracker.close();
		System.out.println("Stopping Bundle");
	}
	
	public class KitchenModuleTrackerCustomizer implements ServiceTrackerCustomizer {
		
		public BundleContext context;
		
		public KitchenModuleTrackerCustomizer(BundleContext context) {
		
		}
		
		@Override
		public Object addingService(ServiceReference reference) {
			KitchenModule module = (KitchenModule) context.getService(reference);
			module.start(new Kitchen() {
				@Override
				public void registerEventConsumer(EventConsumer consumer) {
					
				}
				
				@Override
				public <T> EventPublisher<T> getEventPublisher(Class<T> eventType) {
					return new EventPublisher<T>() {
						@Override
						public void publish(T event) {
							System.out.println("Event received");
						}
					};
				}
			});
			return module;
		}
		
		@Override
		public void modifiedService(ServiceReference reference, Object service) {
			
		}
		
		@Override
		public void removedService(ServiceReference reference, Object service) {
			((KitchenModule) service).stop();
		}
	}
}