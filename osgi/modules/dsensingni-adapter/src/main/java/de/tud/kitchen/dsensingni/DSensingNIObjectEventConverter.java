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
import de.tud.kitchen.api.event.tuio.TangibleEvent;

public class DSensingNIObjectEventConverter implements OSCListener {

	private static final int HAND_EVENT_RANGE_START = 715827882;
	private static final int FINGER_EVENT_RANGE_START = 1431655764;
	
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
