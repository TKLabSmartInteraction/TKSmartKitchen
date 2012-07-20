/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.api.tagging;

import de.tud.kitchen.api.event.Event;

public class TaggingEvent extends Event{
	
	String msg = "";

	public TaggingEvent(String sender, String msg) {
		super(sender);
		this.msg = msg;
	}
	
	public String getMsg() {
		return msg;
	}
	
	@Override
	public String toString() {
		return super.toString() + ", message: "+msg;
	}
	
	@Override
	protected String getAdditionalHeader() {
		return ", msg";
	}
	
	@Override
	protected String getAdditionalLog() {
		return String.format(", %s", msg);
	}
	

}
