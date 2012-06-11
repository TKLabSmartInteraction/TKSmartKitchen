package de.tud.kitchen.osc;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class Activator implements BundleActivator {
	public final static String REMOTE_TYPE = "_tk_kitchen._tcp.local.";

	private OSCPortIn port;
	private JmDNS jmdns;

	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle");

		ServiceInfo pairservice = ServiceInfo.create(REMOTE_TYPE, "KitchenAndroidOSCReceiver", 3333, "TK SmartKitchen Project, Android OSC Receiver");
		jmdns.registerService(pairservice);

		port = new OSCPortIn(3333);
		OSCListener listener = new OSCListener() {
			public void acceptMessage(java.util.Date time, OSCMessage message) {
				Object[] sensor_data = message.getArguments();
				System.out.println("OSC: " + sensor_data[0] + " | " + sensor_data[1] + " | " + sensor_data[2] + "  ||  " + sensor_data[3]);
			}
		};
		port.addListener("/android/101", listener);
		port.startListening();
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");

		port.
		port.close();

		jmdns.unregisterAllServices();
		jmdns.close();
	}
}