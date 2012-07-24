/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.osc;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.event.furniture.DoorEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

public class Activator extends KitchenModuleActivator {
	public final static String REMOTE_TYPE = "_tk_kitchen._tcp.local.";

	private OSCPortIn port;
	private JmDNS jmdns;
	private EventPublisher<AccelerometerEvent> publisher;
	
	@Override
	public void start(Kitchen kitchen) {
		System.out.println("Starting Android Bundle");
		publisher = kitchen.getEventPublisher(AccelerometerEvent.class);
		// ----- start jmdns and register service for android ----- //
		try {
			jmdns = JmDNS.create();
			ServiceInfo pairservice = ServiceInfo.create(REMOTE_TYPE, "KitchenAndroidOSCReceiver", 3334,
					"TK SmartKitchen Project, Android OSC Receiver");
			jmdns.registerService(pairservice);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ----- start osc and register listener for data reception ----- //
		try {
			port = new OSCPortIn(3334);
			OSCListener listener = new OSCListener() {
				public void acceptMessage(java.util.Date time, OSCMessage message) {
					Object[] sensor_data = message.getArguments();
					//System.out.println("OSC: " + sensor_data[0] + " | " + sensor_data[1] + " | " + sensor_data[2] + "  ||  " + sensor_data[3]);
					final long timeBase = Integer.MAX_VALUE;
					final AccelerometerEvent<Float> event = new AccelerometerEvent<Float>(message.getAddress(),
							((BigInteger) sensor_data[3]).longValue(), 
							(Float) sensor_data[0], 
							(Float) sensor_data[1],
							(Float) sensor_data[2]);
					publisher.publish(event);
				}
			};
			port.addListener("/android/.*", listener);
			port.startListening();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		System.out.println("Stopping Bundle");

		port.stopListening();
		port.close();

		try {
			jmdns.unregisterAllServices();
			jmdns.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}