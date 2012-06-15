package de.tud.kitchen.api.event;

import static org.easymock.EasyMock.*;
import org.junit.Test;

public class EventTest {
	
	@Test
	public void dispatchTest() {
		//SETUP
		EventConsumer eventConsumer = createMockBuilder(EventConsumer.class).addMockedMethod("handleObject").withConstructor().createMock();
		Event event = new Event("testSensor") {};
		eventConsumer.handleObject(event);
		replay(eventConsumer);
		//TEST
		event.dispatchTo(eventConsumer);
		//CHECK
		verify(eventConsumer);
	}
}
