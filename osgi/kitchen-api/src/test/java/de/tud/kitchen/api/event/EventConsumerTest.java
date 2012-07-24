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

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import org.junit.Test;

public class EventConsumerTest {

	@Test
	public void testReflectiveDispatchInSubclass() {
		// SETUP
		EventConsumerSubclass consumer = createMockBuilder(EventConsumerSubclass.class)
				.addMockedMethod("handleTestEvent").withConstructor().createMock();
		TestEvent testEvent = new TestEvent();
		consumer.handleTestEvent(testEvent);
		expectLastCall().once();
		replay(consumer);
		// TEST
		consumer.handle(testEvent);
		// VERIFY
		verify(consumer);
	}

	@Test
	public void testReflectiveDispatchInDowncastedSubclass() {
		// SETUP
		EventConsumerSubclass consumer = createMockBuilder(EventConsumerSubclass.class)
				.addMockedMethod("handleTestEvent").withConstructor().createMock();
		TestEvent testEvent = new TestEvent();
		consumer.handleTestEvent(testEvent);
		expectLastCall().once();
		replay(consumer);
		EventConsumer downcastConsumer = consumer;
		Event downcastEvent = testEvent;
		// TEST
		downcastConsumer.handle(downcastEvent);
		// VERIFY
		verify(consumer);
	}

	@Test
	public void testReflectiveDispatchWithAnonymousEventClass() {
		// SETUP
		EventConsumerSubclassWithoutTestEventMethod consumer = createMockBuilder(
				EventConsumerSubclassWithoutTestEventMethod.class).addMockedMethod("handleEvent").withConstructor()
				.createMock();
		Event event = new Event("testSender") {
			@Override
			protected String getAdditionalHeader() {
				return "";
			}

			@Override
			protected String getAdditionalLog() {
				return "";
			}
		};
		consumer.handleEvent(event);
		expectLastCall().once();
		replay(consumer);
		// TEST
		consumer.handle(event);
		// VERIFY
		verify(consumer);
	}

	@Test
	public void testReflectiveDispatchWithLocalEventClass() {
		// SETUP
		EventConsumerSubclass consumer = createMockBuilder(EventConsumerSubclass.class)
				.addMockedMethod("handleTestEvent").withConstructor().createMock();
		LocalTestEvent event = new LocalTestEvent() {
		};
		consumer.handleTestEvent(event);
		expectLastCall().once();
		replay(consumer);
		// TEST
		consumer.handle(event);
		// VERIFY
		verify(consumer);
	}

	public class LocalTestEvent extends TestEvent {
	}

	@Test
	public void testReflectiveDispatchWithLocalEventConsumer() {
		// SETUP
		LocalEventConsumer consumer = createMockBuilder(LocalEventConsumer.class).addMockedMethod("handleEvent")
				.withConstructor(this).createMock();
		Event event = new Event("testSender") {
			@Override
			protected String getAdditionalHeader() {
				return "";
			}

			@Override
			protected String getAdditionalLog() {
				return "";
			}
		};
		consumer.handleEvent(event);
		expectLastCall().once();
		replay(consumer);
		// TEST
		consumer.handle(event);
		// VERIFY
		verify(consumer);
	}

	public class LocalEventConsumer extends EventConsumer {
		public void handleEvent(Event event) {
		}
	}

	@Test
	public void testClassHierarchyBasedDispatch() {
		// SETUP
		EventConsumerSubclassWithoutTestEventMethod consumer = createMockBuilder(
				EventConsumerSubclassWithoutTestEventMethod.class).addMockedMethod("handleEvent").withConstructor()
				.createMock();
		Event event = new Event("testSensor") {
			@Override
			protected String getAdditionalHeader() {
				return "";
			}

			@Override
			protected String getAdditionalLog() {
				return "";
			}
		};
		TestEvent testEvent = new TestEvent();
		consumer.handleEvent(event);
		consumer.handleEvent(testEvent);
		replay(consumer);
		// TEST
		consumer.handle(event);
		consumer.handle(testEvent);
		// VERIFY
		verify(consumer);
	}
	
	
	@Test
	public void testHandlesCheck() {
		//SETUP
		EventConsumerSubclass consumer = new EventConsumerSubclass();
		//TEST
		assertTrue(consumer.handles(TestEvent.class));
		assertFalse(consumer.handles(Event.class));
		assertTrue(consumer.handles(LocalTestEvent.class));
	}

}

class TestEvent extends Event {
	public TestEvent() {
		super("testEvent");
	}
	@Override
	protected String getAdditionalHeader() {
		return "";
	}

	@Override
	protected String getAdditionalLog() {
		return "";
	}
}

class EventConsumerSubclass extends EventConsumer {

	public void handleTestEvent(TestEvent event) {

	}

}

class EventConsumerSubclassWithoutTestEventMethod extends EventConsumer {

	public void handleEvent(Event e) {

	}
}
