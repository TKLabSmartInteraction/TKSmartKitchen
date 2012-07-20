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
import de.tud.kitchen.api.event.tuio.BlobEvent;
import de.tud.kitchen.api.event.tuio.FingerEvent;
import de.tud.kitchen.api.event.tuio.HandEvent;

public class DSensingNICursorEventConverter implements OSCListener {

	private static final int HAND_EVENT_RANGE_START = 715827882;
	private static final int FINGER_EVENT_RANGE_START = 1431655764;
	
	private final EventPublisher<BlobEvent> blobEventPublisher;
	private final EventPublisher<FingerEvent> fingerEventPublisher;
	private final EventPublisher<HandEvent> handEventPublisher;
	
	public DSensingNICursorEventConverter(Kitchen kitchen) {
		blobEventPublisher = kitchen.getEventPublisher(BlobEvent.class);
		fingerEventPublisher = kitchen.getEventPublisher(FingerEvent.class);
		handEventPublisher = kitchen.getEventPublisher(HandEvent.class);
	}
	
	@Override
	public void acceptMessage(Date arg0, OSCMessage arg1) {
		Object[] arguments = arg1.getArguments();
		if(arguments[0].equals("set")){
			int id = ((Integer) arguments[1]);
			if (id < HAND_EVENT_RANGE_START)
				blobEventPublisher.publish(createBlobEvent(id, arguments));
			else if (id < FINGER_EVENT_RANGE_START)
				handEventPublisher.publish(createHandEvent(id - HAND_EVENT_RANGE_START, arguments));
			else
				fingerEventPublisher.publish(createFingerEvent(id - FINGER_EVENT_RANGE_START,arguments));
				
		}
	}

	private static final HandEvent createHandEvent(int id, Object[] arguments) {
//		for (int i = 9; i < arguments.length; i++) {		
//			Object object = arguments[i];
//			System.out.print(i+": "+object+" ");
//		}
//		System.out.println("");
		return new HandEvent(createSenderId(id, "hand"),			//id
							System.currentTimeMillis(), 			//time
							createPoint3f(arguments, 2,3,4),		//position 
							createPoint4f(arguments, 5, 6, 7, 8), 	//velocity
							id,										//handId
							(Float)arguments[9],					//tableDistance
							createPoint3f(arguments, 13, 14, 15));	//pointing Direction
	}
	
	private static final BlobEvent createBlobEvent(int id, Object[] arguments) {
		return new BlobEvent(createSenderId(id, "blob"),			//id
							System.currentTimeMillis(),				//time
							createPoint3f(arguments, 2, 3, 4),		//position
							createPoint4f(arguments, 5, 6, 7, 8),	//velocity
							(Float)arguments[9],					//tableDistance
							(Integer) arguments[13]);				//tangibleObjectId
	}

	private static final FingerEvent createFingerEvent(int id, Object[] arguments) {
		return new FingerEvent(createSenderId(id, "finger"),		//id
							  System.currentTimeMillis(),			//time
							  createPoint3f(arguments, 2, 3, 4),	//position
							  createPoint4f(arguments, 5, 6, 7, 8),	//velocity
							  (Float)arguments[9],					//tableDistance
							  (Integer) arguments[13]);				//handId
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
