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

package de.tud.kitchen.phidgets;

import com.phidgets.PhidgetException;
import com.phidgets.RFIDPhidget;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.TagGainEvent;
import com.phidgets.event.TagGainListener;
import com.phidgets.event.TagLossEvent;
import com.phidgets.event.TagLossListener;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.rfid.RfidEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

/**
 * 
 * @author Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 * 
 */
public class Activator extends KitchenModuleActivator implements TagGainListener, TagLossListener, AttachListener {
	
	private final int SERIALNO = 103075; // serial number of opened RFID-phidget
	
	private EventPublisher<RfidEvent> publisher;
	private RFIDPhidget rfid;

	/**
	 * Set to true if you want to switch on the readers onboard led while a rfid tag is gained.
	 */
	private boolean useRfidLed = true;

	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(RfidEvent.class);

		try {
			rfid = new RFIDPhidget();

			// add Listener to the RFID Phidget
			rfid.addAttachListener(this);
			rfid.addTagGainListener(this);
			rfid.addTagLossListener(this);

			// connect to the RFID reader
			rfid.open(SERIALNO); // identify RFID-phidget by serial number
			//rfid.waitForAttachment(1000); // this blocking call is not reasonable in conjunction with attached-event
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			// Switch of the onboard led and close the rfid reader.
			if(rfid.isAttached())
				rfid.setLEDOn(false);
			rfid.close();
		} catch (PhidgetException e) {
			e.printStackTrace();
		}

		rfid = null;
	}

	@Override
	public void attached(AttachEvent ae) {
		try {
			// activate the readers antenna and switch of the onboard led
			((RFIDPhidget) ae.getSource()).setAntennaOn(true);
			((RFIDPhidget) ae.getSource()).setLEDOn(false);
		} catch (PhidgetException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void tagGained(TagGainEvent tag_gain) {
		try {
			// Publish the tag gain with an rfid event.
			RfidEvent event = new RfidEvent(tag_gain.getSource().getSerialNumber(), true, tag_gain.getValue());
			publisher.publish(event);

			if (useRfidLed) {
				((RFIDPhidget) tag_gain.getSource()).setLEDOn(true);
			}
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void tagLost(TagLossEvent tag_loss) {
		try {
			// Publish the tag loss with an rfid event.
			RfidEvent event = new RfidEvent(tag_loss.getSource().getSerialNumber(), false, tag_loss.getValue());
			publisher.publish(event);

			if (useRfidLed) {
				((RFIDPhidget) tag_loss.getSource()).setLEDOn(false);
			}
		} catch (PhidgetException e) {
			e.printStackTrace();
		}
	}
}