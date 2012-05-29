package de.tud.kitchen.api.event.acc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.event.acc.AccelerometerEventConsumer;

public class AccelerometerTest {
	
	@Test
	public void testEqual() {
	   AccelerometerEvent<Integer> intAccelData10 = new AccelerometerEvent<Integer>(134134238742L, "testSensor1", 341, 123, 991);
	   AccelerometerEvent<Integer> intAccelData11= new AccelerometerEvent<Integer>(134134238753L, "testSensor1", 340, 121, 880);
	   AccelerometerEvent<Integer> intAccelData20 = new AccelerometerEvent<Integer>(134134238753L, "testSensor2", 340, 121, 880);
	   
	   assertFalse(intAccelData10.equals(null));
	   assertTrue(intAccelData10.equals(intAccelData10));
	   assertFalse(intAccelData10.equals(intAccelData11));
	   assertFalse(intAccelData10.equals(intAccelData20));
	   

	   AccelerometerEvent<Float> floatAccelData10 = new AccelerometerEvent<Float>(134134238742L, "testSensor1", 341.2f, 123.1f, 991.0f);
	   AccelerometerEvent<Float> floatAccelData11= new AccelerometerEvent<Float>(134134238753L, "testSensor1", 340.0f, 121.2f, 880.3f);
	   AccelerometerEvent<Float> floatAccelData20 = new AccelerometerEvent<Float>(134134238753L, "testSensor2", 340.0f, 121.2f, 880.3f);
	   
	   assertFalse(floatAccelData10.equals(null));
	   assertTrue(floatAccelData10.equals(floatAccelData10));
	   assertFalse(floatAccelData10.equals(floatAccelData11));
	   assertFalse(floatAccelData10.equals(floatAccelData20));
	   
	   AccelerometerEvent<Double> doubleAccelData10 = new AccelerometerEvent<Double>(134134238742L, "testSensor1", 341.2, 123.1, 991.0);
	   AccelerometerEvent<Double> doubleAccelData11= new AccelerometerEvent<Double>(134134238753L, "testSensor1", 340.0, 121.2, 880.3);
	   AccelerometerEvent<Double> doubleAccelData20 = new AccelerometerEvent<Double>(134134238753L, "testSensor2", 340.0, 121.2, 880.3);
	   
	   assertFalse(doubleAccelData10.equals(null));
	   assertTrue(doubleAccelData10.equals(doubleAccelData10));
	   assertFalse(doubleAccelData10.equals(doubleAccelData11));
	   assertFalse(doubleAccelData10.equals(doubleAccelData20));
	   
	}
	
	boolean accelerometerConsumerCalled = false;
	@Test
	public void doubleDispatchTest() {
		AccelerometerEvent<Float> testEvent = new AccelerometerEvent<Float>(134134238742L, "testSensor1", 341.2f, 123.1f, 991.0f);
		Event downCastEvent = testEvent;
		
		AccelerometerEventConsumer<Float> accelerometerEventConsumer = new AccelerometerEventConsumer<Float>() {
			@Override
			public void handleAccelerometerEvent(AccelerometerEvent<Float> event) {
				accelerometerConsumerCalled = true;
			}
		};
		
		EventConsumer downcastAccelerometerConsumer = accelerometerEventConsumer;
		downCastEvent.dispatchTo(downcastAccelerometerConsumer);
		
		assertTrue("Accelerometer Consumer not called", accelerometerConsumerCalled);
		
	}
	
}
