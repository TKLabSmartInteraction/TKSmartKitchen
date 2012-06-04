package de.tud.kitchen.dsensingni;

import java.util.Date;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.tuio.HandEvent;

public class DSensingNICursorEventConverter implements OSCListener {

	private static final int HAND_EVENT_RANGE_START = 715827882;
	private static final int FINGER_EVENT_RANGE_START = 1431655764;
	
	private final Kitchen kitchen;
	
	public DSensingNICursorEventConverter(Kitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void acceptMessage(Date arg0, OSCMessage arg1) {
		Object[] arguments = arg1.getArguments();
		if(arguments[0].equals("set")){
			int id = ((Integer) arguments[1]);
			if (id < HAND_EVENT_RANGE_START)
				blobEvent(id, arguments);
			else if (id < FINGER_EVENT_RANGE_START)
				System.out.println(createHandEvent(id - HAND_EVENT_RANGE_START, arguments));
			else
				fingerEvent(id - FINGER_EVENT_RANGE_START);
				
		}
	}

	private static final HandEvent createHandEvent(int id, Object[] arguments) {
		for (int i = 9; i < arguments.length; i++) {		
			Object object = arguments[i];
			System.out.print(i+": "+object+" ");
		}
		System.out.println("");
		return new HandEvent(createSenderId(id, "hand"),			//id
							System.currentTimeMillis(), 			//time
							createPoint3f(arguments, 2,3,4),		//position 
							createPoint4f(arguments, 5, 6, 7, 8), 	//velocity
							(Float)arguments[9],					//tableDistance
							createPoint3f(arguments, 13, 14, 15));	//pointing Direction
	}
	
	private static Point3f createPoint3f(Object[] arguments, int x, int y, int z) {
		return new Point3f((Float)arguments[x],(Float)arguments[y],(Float)arguments[z]);
	}
	
	private static Point4f createPoint4f(Object[] arguments, int x, int y, int z, int w) {
		return new Point4f((Float)arguments[x],(Float)arguments[y],(Float)arguments[z],(Float)arguments[w]);
	}

	private static final String createSenderId(final int id, final String type) {
		return "kinect/" + type + "/" + id;
	}
	
	private void blobEvent(int id, Object[] arguments) {
		// TODO Auto-generated method stub
		
	}

	private void fingerEvent(int i) {
		// TODO Auto-generated method stub
	}
	
}
