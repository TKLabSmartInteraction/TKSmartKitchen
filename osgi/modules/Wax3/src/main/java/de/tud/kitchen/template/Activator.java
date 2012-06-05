package de.tud.kitchen.template;

import java.net.SocketException;
import java.util.Date;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPort;
import com.illposed.osc.OSCPortIn;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	OSCPortIn port;
	EventPublisher<AccelerometerEvent> publisher;
	
	@Override
	public void start(Kitchen kitchen) {
		try {
			port = new OSCPortIn(OSCPort.defaultSCOSCPort());
			publisher = kitchen.getEventPublisher(AccelerometerEvent.class);
			port.addListener("/wax3/101", new parametricOscListener("101"));
			port.addListener("/wax3/102", new parametricOscListener("102"));
			port.addListener("/wax3/103", new parametricOscListener("103"));
			port.addListener("/wax3/104", new parametricOscListener("104"));
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void stop() {
		port.close();		
	}
	
	private class parametricOscListener implements OSCListener {
		String source;		
		public parametricOscListener(String source){
			super();
			this.source = source;			
		}		
		@Override
		public void acceptMessage(Date arg0, OSCMessage arg1) {
			publisher.publish(new AccelerometerEvent<Float>(source, System.currentTimeMillis(), 
					new Float(arg1.getArguments()[0].toString()), 
					new Float(arg1.getArguments()[1].toString()), 
					new Float(arg1.getArguments()[2].toString())));				
		}
		
	}
}