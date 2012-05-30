package de.tud.kitchen.api.event;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Test;

public class EventConsumerTest {

	@Test
	public void testReflectiveDispatchInSubclass() {
		//SETUP
		EventConsumerSubclass consumer = createMockBuilder(EventConsumerSubclass.class).addMockedMethod("handleTestEvent").createMock();
		TestEvent testEvent = new TestEvent();
		consumer.handleTestEvent(testEvent);
		expectLastCall().once();
		replay(consumer);
		//TEST
		testEvent.dispatchTo(consumer);
		//VERIFY
		verify(consumer);
	}
	
	@Test
	public void testReflectiveDispatchInDowncastedSubclass() {
		//SETUP
		EventConsumerSubclass consumer = createMockBuilder(EventConsumerSubclass.class).addMockedMethod("handleTestEvent").createMock();
		TestEvent testEvent = new TestEvent();
		consumer.handleTestEvent(testEvent);
		expectLastCall().once();
		replay(consumer);
		EventConsumer downcastConsumer = consumer;
		Event downcastEvent = testEvent;
		//TEST
		downcastEvent.dispatchTo(downcastConsumer);
		//VERIFY
		verify(consumer);
	}
	
	@Test 
	public void testClassHierarchyBasedDispatch() {
		//SETUP
		EventConsumerSubclassWithoutTestEventMethod consumer = createMockBuilder(EventConsumerSubclassWithoutTestEventMethod.class).addMockedMethod("handleEvent").createMock();
		Event event = new Event("testSensor") {}; 
		TestEvent testEvent = new TestEvent();
		consumer.handleEvent(event);
		consumer.handleEvent(testEvent);
		replay(consumer);
		//TEST
		event.dispatchTo(consumer);
		testEvent.dispatchTo(consumer);
		//VERIFY
		verify(consumer);
	}

}

class TestEvent extends Event {
	public TestEvent() {
		super("testEvent");
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


