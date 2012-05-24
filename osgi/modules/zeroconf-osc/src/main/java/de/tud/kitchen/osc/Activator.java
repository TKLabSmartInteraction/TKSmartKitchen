package de.tud.kitchen.osc;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCPortIn;

public class Activator implements BundleActivator {
	OSCPortIn port;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle");
		port = new OSCPortIn(3333);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");
		port.close();
	}
}