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

package de.tud.kitchen.analyser.basics;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tud.kitchen.analyser.basics.Averager.DoubleAverager;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public class AveragerTest {
	public static final String sender = "/tag/100";

	@Test
	public void testDoubleAverager() {
		
		DoubleAverager uut = new DoubleAverager(sender, 5);
		assertTrue(uut.seenEvents.isEmpty());
		AccelerometerEvent<Double> testEvent1 = createTestEvent(6.0, 1.0, 3.0);
		AccelerometerEvent<Double> testEvent2 = createTestEvent(5.0, 2.0, 3.0);
		AccelerometerEvent<Double> testEvent3 = createTestEvent(4.0, 3.0, 3.0);
		AccelerometerEvent<Double> testEvent4 = createTestEvent(3.0, 4.0, 3.0);
		AccelerometerEvent<Double> testEvent5 = createTestEvent(2.0, 5.0, 3.0);
		AccelerometerEvent<Double> testEvent6 = createTestEvent(1.0, 6.0, 3.0);
		AccelerometerEvent<Double> avgEvent = uut.push(testEvent1);
		assertEquals("/tag/100/avg", avgEvent.sender);
		assertEquals(testEvent1.timestamp, avgEvent.timestamp);
		assertAccelerometerEvent(6.0, 1.0, 3.0, avgEvent);
		
		avgEvent = uut.push(testEvent2);
		assertEquals("/tag/100/avg", avgEvent.sender);
		assertEquals(testEvent2.timestamp, avgEvent.timestamp);
		assertAccelerometerEvent(5.5, 1.5, 3.0, avgEvent);
		
		avgEvent = uut.push(testEvent3);
		assertAccelerometerEvent(5.0, 2.0, 3.0, avgEvent);
		
		avgEvent = uut.push(testEvent4);
		assertAccelerometerEvent(4.5, 2.5, 3.0, avgEvent);
		
		avgEvent = uut.push(testEvent5);
		assertAccelerometerEvent(4.0, 3.0, 3.0, avgEvent);

		avgEvent = uut.push(testEvent6);
		assertAccelerometerEvent(3.0, 4.0, 3.0, avgEvent);

	}
	
	
	public void assertAccelerometerEvent(Double x, Double y, Double z, AccelerometerEvent<Double> actual) {
		assertEquals(x, actual.x);
		assertEquals(y, actual.y);
		assertEquals(z, actual.z);
	}
	
	public AccelerometerEvent<Double> createTestEvent(double x, double y, double z) {
		return new AccelerometerEvent<Double>(sender, System.currentTimeMillis(), x, y, z);
	}
	
}
