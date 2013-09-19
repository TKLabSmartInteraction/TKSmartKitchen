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
	
	boolean isActive;
	String actionName;
	
	public BlenderEvent(String sender, String actionName, boolean isActive) {		
		super(sender);
		int buttonNumber=new Integer(actionName);
		switch (buttonNumber) {
		case 00:
			this.actionName = "Blending-Dialed";
			break;
		case 01:
			this.actionName = "Smoothy-Button";
			break;
		case 02:
			this.actionName = "Ice-Button";
			break;
		case 04:
			this.actionName = "Pulse-Button";
			break;
		default:
			this.actionName = "Blending-Dialed";
			break;
		}
		this.isActive = isActive;
	}
	
	public BlenderEvent(String sender) {
		super(sender);
	}


	@Override
	public String toString() {
		return "Action "+this.actionName+(isActive ? " active" : " inactive");
	}

	@Override
	protected String getAdditionalCsvValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return String.format(", %s, %s", actionName ,((isActive)?"active":"inactive"));
	}

	
	

}
