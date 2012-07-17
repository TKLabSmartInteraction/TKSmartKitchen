package de.tud.kitchen.main.impl;

import static junit.framework.Assert.assertTrue;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.event.EventPublisher;

public class KitchenImplTest {

	private Kitchen kitchen;
	
	@Before
	public void beforeTest() {
		kitchen = new KitchenImpl();
	}
	
	@Test
	public void testEventConsumerRegistration() {
		//TEST
		kitchen.registerEventConsumer(new TestEventConsumer());
	}
	
	@Test
	public void testAcquireEventPublisher() {
		//TEST
		EventPublisher<TestEvent> pub = kitchen.getEventPublisher(TestEvent.class);
		//VERIFY
		assertTrue(pub!=null);
	}
	
	@Test
	public void testCorrectEventDeliveryOneToOne() {
		//SETUP
		EventPublisher<TestEvent> pub = kitchen.getEventPublisher(TestEvent.class);
		TestEventConsumer consumer = createMockBuilder(TestEventConsumer.class).withConstructor(this).addMockedMethod("handleEvent").createMock();
		kitchen.registerEventConsumer(consumer);
		TestEvent testEvent = new TestEvent();
		consumer.handleEvent(testEvent);
		replay(consumer);
		//TEST
		pub.publish(testEvent);
		//VERIFY
		verify(consumer);
	}
	
	@Test
	public void testCorrectEventDeliveryOneToMany() {
		//SETUP
		EventPublisher<TestEvent> pub = kitchen.getEventPublisher(TestEvent.class);
		TestEvent testEvent = new TestEvent();
		TestEventConsumer[] eventConsumers = new TestEventConsumer[10];
		for (int i = 0; i < eventConsumers.length; i++) {
			final TestEventConsumer newConsumer = createMockBuilder(TestEventConsumer.class).withConstructor(this).addMockedMethod("handleEvent").createMock();
			eventConsumers[i] = newConsumer;
			kitchen.registerEventConsumer(newConsumer);
			newConsumer.handleEvent(testEvent);
			replay(newConsumer);
		}
		//TEST
		pub.publish(testEvent);
		//VERIFY
		verify((Object[]) eventConsumers);
	}
	
	@Test
	public void testCorrectEventDeliveryManyToOne() {
		//SETUP
		TestEvent testEvent = new TestEvent();
		TestEventConsumer consumer = createMockBuilder(TestEventConsumer.class).withConstructor(this).addMockedMethod("handleEvent").createMock();
		EventPublisher<?>[] eventPublishers = new EventPublisher<?>[10];
		for (int i = 0; i < eventPublishers.length; i++) {
			eventPublishers[i] = kitchen.getEventPublisher(TestEvent.class);
		}
		kitchen.registerEventConsumer(consumer);
		consumer.handleEvent(testEvent);
		expectLastCall().times(10);
		replay(consumer);
		//TEST
		for (int i = 0; i < eventPublishers.length; i++) {
			@SuppressWarnings("unchecked")
			EventPublisher<TestEvent> eventPublisher = (EventPublisher<TestEvent>) eventPublishers[i];
			eventPublisher.publish(testEvent);
		}
		//VERIFY
		verify(consumer);
	}
	
	public class TestEventConsumer extends EventConsumer {
		public void handleEvent(Event event) {
			
		}
	}
	
	public class TestEvent extends Event {
		public TestEvent() {
			super("testSender");
		}
		@Override
		protected String getAdditionalHeader() {
			return "";
		}

		@Override
		protected String getAdditionalLog() {
			return "";
	}
	
	public class WrongEventConsumer extends EventConsumer {
		@Override
		public void handleObject(Event event) {
			Assert.fail();
		}
		
		public void handleWrongEvent(Event event) {
		}
	}
	
	public class WrongEvent extends Event {
		public WrongEvent() {
			super("wrongSender");
		}
	}
}
