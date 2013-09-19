
package de.tud.kitchen.wax.felix;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.serialprovider.*;
import de.tud.kitchen.wax.WAXListener;
import de.tud.kitchen.wax.WAXPacket;
import de.tud.kitchen.wax.WAXReceiver;

public class Activator extends KitchenModuleActivator implements WAXListener, SPServiceEndpoint {

	@SuppressWarnings("rawtypes")	
	EventPublisher<AccelerometerEvent> publisher;
	SerialProvider provider;
	WAXReceiver receiver;
	
	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(AccelerometerEvent.class);
		
		provider = SerialProvider.getInstance();
		provider.requestDevice(new ChallengeResponseString("id\r", "WAX-r"), this);				// identify WAX-receiver by identification string
	}

	@Override
	public void stop() {
		if(receiver instanceof WAXReceiver)
			this.receiver.terminateReceiver();
	}
	
	@Override
	public void serialDeviceAttached(DataInputStream instream,
			DataOutputStream outstream, SPSerialPort port) {
		this.receiver = new WAXReceiver(instream);
		this.receiver.addListener(this);
		System.out.println("WAXReceiver: Serial device attached!");
	}
	
	@Override
	public void serialDeviceRemoved(DataInputStream instream,
			DataOutputStream outstream, SPSerialPort port) {
		this.receiver.terminateReceiver();
		provider.requestDevice(new ChallengeResponseString("id\r", "WAX-r"), this); // issue new device request
		System.out.println("WAXReceiver: Serial device detached!");
	}
	
	@Override
	public void receiveWAX(WAXPacket packet) {
		for(int i=0; i<packet.getContainedSamples();i++){
			publisher.publish(new AccelerometerEvent<Float>(new Integer(packet.getDeviceID()).toString(), packet.getSample(i).getTimestamp(), packet.getSample(i).getAcceleration().x, packet.getSample(i).getAcceleration().y, packet.getSample(i).getAcceleration().z));
		}
	}
	
}