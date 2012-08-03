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

package de.tud.kitchen.blenderReceiver;

import de.tud.kitchen.api.event.Event;

/**
 * represents a button push or release
 * @author Christian Klos
 *
 */
public class BlenderEvent extends Event {
	
	boolean pushed;
	String prettyButtonName;
	
	public BlenderEvent(String sender, boolean pushed) {		
		super(sender);
		int buttonNumber=new Integer(sender);
		switch (buttonNumber) {
		case 01:
			prettyButtonName = "Smoothy";
			break;
		case 02:
			prettyButtonName = "Ice";
			break;
		case 04:
			prettyButtonName = "Pulse";
			break;
		case 8:
			prettyButtonName = "Dial-on";
			break;
		case 10:
			prettyButtonName = "Dial-Min...Max";
			break;		
		case 20:
			prettyButtonName = "Motor";
			break;
		default:
			prettyButtonName = sender;
			break;
		}
		
		this.pushed = pushed;
	}
	
	public BlenderEvent(String sender) {
		super(sender);
	}


	@Override
	public String toString() {
		return "Button "+this.prettyButtonName+(pushed ? " pushed" : " released");
	}

	@Override
	protected String getAdditionalCsvValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return String.format(", %s", ((pushed)?"pushed":"pushed"));
	}

	
	

}
