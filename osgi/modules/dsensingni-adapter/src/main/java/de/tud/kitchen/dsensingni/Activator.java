package de.tud.kitchen.dsensingni;


import java.net.SocketException;

import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCPortIn;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	
	private OSCPortIn dsensingni;
	
	@Override
	public void start(BundleContext context) throws Exception {
//		System.out.println("DsensingNI Bundle start called");
		super.start(context);
	}

	@Override
	public void start(Kitchen kitchen) {
		try {
			dsensingni = new OSCPortIn(3333);
			dsensingni.addListener("/tuio/3Dcur", new DSensingNICursorEventConverter(kitchen));
			dsensingni.addListener("/tuio/3Dblb", new DSensingNIObjectEventConverter(kitchen));
			dsensingni.startListening();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		dsensingni.stopListening();
		dsensingni.close();
		dsensingni = null;
	}
}