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

package de.tud.kitchen.newArduino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.TooManyListenersException;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.furniture.DoorEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;
import de.tud.kitchen.arduino.Arduino;
import de.tud.kitchen.arduino.DoorSensorEvent;
import de.tud.kitchen.arduino.SensorEventListener;
import de.tud.kitchen.serialprovider.SPSerialPort;
import de.tud.kitchen.serialprovider.SPServiceEndpoint;
import de.tud.kitchen.serialprovider.SerialProvider;
import de.tud.kitchen.serialprovider.ChallengeResponse;;

/**
 * the Activator for the Arduino-board thats used to send
 * door activities
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator implements SPServiceEndpoint{
	SerialProvider provider = SerialProvider.getInstance();
	
	//get an instance of the board
	Arduino ard = Arduino.getInstance();
	//and create an eventPublisher
	EventPublisher<DoorEvent> eventpublisher;

	@Override
	public void start(Kitchen kitchen) {	
		// assign the eventpublisher
		 eventpublisher = kitchen.getEventPublisher(DoorEvent.class);
		 provider.requestDevice(new ChallengeResponse(new byte[]{0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x50}, new byte[]{0x41,0x42,0x43,0x44,0x45,0,0,0,0,0}, 1000, 50), this);	// identify arduino board by partial echo (exact matching-mode)

		 /* create and add a sensorEventListener thats called from the 
		  Arduino class and that publishes the DoorEvent to a kitchen
		  compatible one.
		  */
		 ard.addSensorEventListener(new SensorEventListener() {
			
			@Override
			public void SensorEvent(DoorSensorEvent arg0) {
				DoorEvent event = new DoorEvent(arg0.getSensor(), arg0.getValue());
				eventpublisher.publish(event);			
			}
		});
		 
	}

	@Override
	public void stop() {
	}
	
	@Override
	public void serialDeviceAttached(DataInputStream instream,
			DataOutputStream outstream, SPSerialPort port) {
		System.out.println("Arduino: Serial device attached!");
		ard.connect(instream, outstream); // connect to the board and set it in the correct state
		try {
			port.addSerialPortEventListener(ard);
		} catch (TooManyListenersException e) {
			System.err.println("Arduino: Too many listeners exception!");
			e.printStackTrace();
		}
		try {
			Thread.sleep(1000);	// wait for Arduino-board to become ready again after reset
		} catch (InterruptedException e) {
		}
		ard.switchMode(Arduino.MCHANGES);
//		try {
//			if(instream.available() >= 1)
//				instream.skipBytes(instream.available());	// skip all bytes in device buffer
//		} catch (IOException e) {
//			System.err.println("Arduino: I/O-Exception!");
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void serialDeviceRemoved(DataInputStream instream,
			DataOutputStream outstream, SPSerialPort port) {
		provider.requestDevice(new ChallengeResponse(new byte[]{0x41,0x42,0x43,0x44,0x45,0x46,0x47,0x48,0x49,0x50}, new byte[]{0x41,0x42,0x43,0x44,0x45,0,0,0,0,0}, 1000, 50), this);	// identify arduino board by partial echo (exact matching-mode)
	}
	
}
