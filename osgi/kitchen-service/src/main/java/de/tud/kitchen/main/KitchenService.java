package de.tud.kitchen.main;

import org.osgi.framework.BundleContext;

import de.tud.kitchen.api.Kitchen;

public class KitchenService implements Kitchen {

	private BundleContext context;
	
	public KitchenService(BundleContext context) {
		this.context = context;
	}
}
