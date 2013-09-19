package de.tud.kitchen.serialprovider.felix;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.Hashtable;

import de.tud.kitchen.serialprovider.SerialProviderBackend;

public class Activator implements BundleActivator {

	private SerialProviderBackend providerInstance = null;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void start(BundleContext context) {
		
		Hashtable props = new Hashtable();
		props.put("osgi.command.scope", "serial");
		props.put("osgi.command.function", new String[] {"listdevices","listbindings","listpending","stacktrace","status"});
		context.registerService(SPGogoCommands.class.getName(), new SPGogoCommands(context), props);
		
		providerInstance = SerialProviderBackend.getInstance(); // get reference to SerialProvider object
		providerInstance.start();								// start device controller thread
	}
	
	@Override
	public void stop(BundleContext context) {
		providerInstance.stop();								// stop device controller thread
	}
	
}
