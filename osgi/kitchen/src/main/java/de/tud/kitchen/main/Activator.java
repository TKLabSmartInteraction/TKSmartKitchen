package de.tud.kitchen.main;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle");
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");
	}
}