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

package de.tud.kitchen.dsensingni;

import java.util.Date;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.tuio.TangibleEvent;

public class DSensingNIObjectEventConverter implements OSCListener {
	
	private final EventPublisher<TangibleEvent> tangibleEventPublisher;
	
	public DSensingNIObjectEventConverter(Kitchen kitchen) {
		tangibleEventPublisher = kitchen.getEventPublisher(TangibleEvent.class);
	}
	
	@Override
	public void acceptMessage(Date arg0, OSCMessage arg1) {
		Object[] arguments = arg1.getArguments();
		if(arguments[0].equals("set")) {
			tangibleEventPublisher.publish(createTangibleEvent(arguments));	
		}
	}
	
	private TangibleEvent createTangibleEvent(Object[] arguments) {
		return new TangibleEvent(createSenderId((Integer) arguments[1], "tangible"), 
								System.currentTimeMillis(),
								createPoint3f(arguments, 2, 3, 4), 
								createPoint4f(arguments, 12, 13, 14, 18), 
								(Float) arguments[20],
								createPoint3f(arguments, 8, 9, 10),
								(Integer) arguments[24],
								(Integer) arguments[25]);
	}

	private static final Point3f createPoint3f(Object[] arguments, int x, int y, int z) {
		return new Point3f((Float)arguments[x],(Float)arguments[y],(Float)arguments[z]);
	}
	
	private static final Point4f createPoint4f(Object[] arguments, int x, int y, int z, int w) {
		return new Point4f((Float)arguments[x],(Float)arguments[y],(Float)arguments[z],(Float)arguments[w]);
	}

	private static final String createSenderId(final int id, final String type) {
		return "kinect/" + type + "/" + id;
	}
}
