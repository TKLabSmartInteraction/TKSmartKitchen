/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Ständer <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 */

package de.tud.kitchen.steamer.felix;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.module.KitchenModuleActivator;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;

import de.tud.kitchen.steamer.EventCallback;
import de.tud.kitchen.steamer.Steamer;
import de.tud.kitchen.steamer.SteamerEvent;

/**
 * 
 * @author Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 * 
 */
public class Activator extends KitchenModuleActivator implements AttachListener, DetachListener, EventCallback {
	
	private final int SERIALNO = 125635; // serial number of 8/8/8-phidget (Steamer)
	
	private EventPublisher<SteamerEvent> publisher;
	private InterfaceKitPhidget steamerPhidget;
	private Steamer philipsSteamer;
	
	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(SteamerEvent.class);
		try {
			steamerPhidget = new InterfaceKitPhidget();
			steamerPhidget.addAttachListener(this);
			steamerPhidget.addDetachListener(this);
			steamerPhidget.open(SERIALNO); // identify 8/8/8-phidget by serial number
		} catch (PhidgetException e) {
			System.err.println("SteamerReceiver: Device could not be opened - Phidget exception!");
			e.printStackTrace();
		}
		philipsSteamer = new Steamer(steamerPhidget);
	}

	@Override
	public void stop() {
		try {
			steamerPhidget.close();
		} catch (PhidgetException e) {
			System.err.println("SteamerReceiver: Device could not be stopped - Phidget exception!");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.err.println("SteamerReceiver: Device could not be accessed for stopping.");
		}
	}

	@Override
	public void attached(AttachEvent ae) {
		try {
			philipsSteamer.init(); // set all outputs to high -> release all steamer-hw inputs
		} catch (PhidgetException e) {
			System.err.println("SteamerReceiver: Device could not be attached - Phidget exception!");
			e.printStackTrace();
		}
		philipsSteamer.setCallback(this); // set callback after init to avoid turn-on/turn-off event on attachment
	}
	
	@Override
	public void detached(DetachEvent arg0) {
		philipsSteamer.close();
	}
	
	@Override
	public void descale() {
		// No user action -> do nothing
	}
	
	@Override
	public void refill() {
		// No user action -> do nothing
	}
	
	@Override
	public void steamingOff() {
		publisher.publish(new SteamerEvent(SERIALNO, false));
	}
	
	@Override
	public void steamingOn() {
		publisher.publish(new SteamerEvent(SERIALNO, true));
	}
	
}
