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

package de.tud.kitchen.api.event;

import static org.junit.Assert.*;

import org.junit.Test;

import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public class EventConsumerStressTest {

	@Test
	public void testSingleThreadSpeedWithAccelerometerEvents() {
		// SETUP
		AccelerometerEvent<?>[] events = generateRandomAccelerometerEventArray(8000);
		AccelerometerEventConsumer consumer = new AccelerometerEventConsumer();
		// TEST
		long start = System.currentTimeMillis();
		for (AccelerometerEvent<?> accelerometerEvent : events) {
			consumer.handle(accelerometerEvent);
		}
		long time = System.currentTimeMillis() - start;
		// VERIFY
		assertTrue(consumer.received == 8000);
		// CHECK TIME
		System.out.println("testSingleThreadSpeedWithAccelerometerEvents() took "
				+ time + "ms");
		assertTrue(time<50);
	}

	@Test
	public void testSingleThreadSpeedWithLevel5SubclassEvents() {
		// SETUP
		Level5Event[] events = generateLevel5EventArray(8000);
		LevelEventConsumer consumer = new LevelEventConsumer();
		// TEST
		long start = System.currentTimeMillis();
		for (Level5Event levelEvent : events) {
			consumer.handle(levelEvent);
		}
		long time = System.currentTimeMillis() - start;
		// VERIFY
		assertTrue(consumer.receivedLevel5 == 8000);
		// CHECK TIME
		System.out.println("testSingleThreadSpeedWithLevel5SubclassEvents() took "
				+ time + "ms");
		assertTrue(time<50);
	}

	private Level5Event[] generateLevel5EventArray(int numEntries) {
		Level5Event[] events = new Level5Event[numEntries];
		long time = System.currentTimeMillis();
		for (int i = 0; i < numEntries; i++) {
			events[i] = new Level5Event(time);
			time += 10;
		}
		return events;
	}

	private static AccelerometerEvent<?>[] generateRandomAccelerometerEventArray(int numEntries) {
		AccelerometerEvent<?>[] events = new AccelerometerEvent<?>[numEntries];
		long time = System.currentTimeMillis();
		for (int i = 0; i < numEntries; i++) {
			events[i] = generateRandomAccelerometerEvent(time);
			time += 10;
		}
		return events;
	}

	private static AccelerometerEvent<Double> generateRandomAccelerometerEvent(long time) {
		return new AccelerometerEvent<Double>("testSender", time, Math.random(), Math.random(), Math.random());
	}

	public class AccelerometerEventConsumer extends EventConsumer {
		public int received = 0;

		public void handleAccelerometerEvent(AccelerometerEvent<Double> accEvent) {
			received++;
		}
	}

	public class Level1Event extends Event {
		public Level1Event(long time) {
			super("TestSender", time);
		}

		public Level1Event() {
			super("TestSender");
		}
		
		@Override
		protected String getAdditionalCsvHeader() {
			return "";
		}
		
		@Override
		protected String getAdditionalCsvValues() {
			return "";
		}
	}

	public class LevelEventConsumer extends EventConsumer {
		public int receivedLevel5 = 0;

		public void handleLevel5Event(Level5Event event) {
			receivedLevel5++;
		}
	}

	public class Level2Event extends Level1Event {
		public Level2Event(long time) {
			super(time);
		}
	};

	public class Level3Event extends Level2Event {
		public Level3Event(long time) {
			super(time);
		}
	};

	public class Level4Event extends Level3Event {
		public Level4Event(long time) {
			super(time);
		}
	};

	public class Level5Event extends Level4Event {
		public Level5Event(long time) {
			super(time);
		}
	};
}
