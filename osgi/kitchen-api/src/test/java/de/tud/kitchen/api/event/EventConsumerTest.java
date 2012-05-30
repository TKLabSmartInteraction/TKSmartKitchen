package de.tud.kitchen.api.event;

import org.junit.Test;
import static org.easymock.EasyMock.*;

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

}

class TestEvent extends Event {
	public TestEvent() {
		super("testEvent");
	}
}

class EventConsumerSubclass extends EventConsumer {

	@Override
	public void handle(Object o) {
		try {
			getMethod(o.getClass()).invoke(this, new Object[] { o });
		} catch (Exception ex) {
			System.out.println("no appropriate handle() method");
		}
	}
	
	public void handleTestEvent(TestEvent event) {
		
	}

}
