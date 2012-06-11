package de.tud.kitchen.apps.eventinspector;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.TreePath;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.apps.eventinspector.DynamicEventFilter.DynamicEventFilterDelegate;

public class DynamicEventFilterTest {

	
	DynamicEventFilterDelegate delegateMock;
	DynamicEventFilter filter;
	
	@Before
	public void setup() {
		delegateMock = createMock(DynamicEventFilterDelegate.class);
		filter = new DynamicEventFilter(delegateMock);
	}
	
	@Test
	public void testDefaultFilterAllowsNothing() {
		//SETUP
		replay(delegateMock);
		//TEST
		filter.handleEvent(new TestEvent());
		//VERIFY
		verify(delegateMock);
	}
	
	@Test
	public void testFilterForTestEventClass() {
		//SETUP
		TreeSelectionEvent selectionEvent = createNiceMock(TreeSelectionEvent.class);
		TreePath[] treePath = createTreePathArray(TestEvent.class);
		expect(selectionEvent.getPaths()).andReturn(treePath);
		expect(selectionEvent.isAddedPath(treePath[0])).andReturn(true);
		replay(selectionEvent);
		Event event1 = new TestEvent();
		Event event2 = new TestEvent();
		delegateMock.handleEvent(event1);
		delegateMock.handleEvent(event2);
		replay(delegateMock);
		//TEST
		filter.valueChanged(selectionEvent);
		filter.handleEvent(event1);
		filter.handleEvent(event2);
		//VERIFY
		verify(delegateMock);
	}
	
	@Test
	public void testFilterForRemovedTestEventClass() {
		//SETUP
		TreeSelectionEvent selectionEventAdd = createNiceMock(TreeSelectionEvent.class);
		TreeSelectionEvent selectionEventRemove = createNiceMock(TreeSelectionEvent.class);
		TreePath[] treePath = createTreePathArray(TestEvent.class);
		expect(selectionEventAdd.getPaths()).andReturn(treePath);
		expect(selectionEventRemove.getPaths()).andReturn(treePath);
		expect(selectionEventAdd.isAddedPath(treePath[0])).andReturn(true);
		expect(selectionEventRemove.isAddedPath(treePath[0])).andReturn(false);
		replay(selectionEventAdd);
		replay(selectionEventRemove);
		Event event1 = new TestEvent();
		Event event2 = new TestEvent();
		delegateMock.handleEvent(event1);
		replay(delegateMock);
		//TEST
		filter.valueChanged(selectionEventAdd);
		filter.handleEvent(event1);
		filter.valueChanged(selectionEventRemove);
		filter.handleEvent(event2);
		//VERIFY
		verify(delegateMock);
	}
	
	@Test
	public void testIgnoreSubclasses() {
		//SETUP
		TreeSelectionEvent selectionEvent = createNiceMock(TreeSelectionEvent.class);
		TreePath[] treePath = createTreePathArray(TestEvent.class);
		expect(selectionEvent.getPaths()).andReturn(treePath);
		expect(selectionEvent.isAddedPath(treePath[0])).andReturn(true);
		replay(selectionEvent);
		Event event1 = new TestEvent();
		Event event2 = new TestEventSubclass();
		delegateMock.handleEvent(event1);
		replay(delegateMock);
		//TEST
		filter.valueChanged(selectionEvent);
		filter.handleEvent(event1);
		filter.handleEvent(event2);
		//VERIFY
		verify(delegateMock);
	}
	
	@Test
	public void testIgnoreSuperclasses() {
		//SETUP
		TreeSelectionEvent selectionEvent = createNiceMock(TreeSelectionEvent.class);
		TreePath[] treePath = createTreePathArray(TestEventSubclass.class);
		expect(selectionEvent.getPaths()).andReturn(treePath);
		expect(selectionEvent.isAddedPath(treePath[0])).andReturn(true);
		replay(selectionEvent);
		Event event1 = new TestEvent();
		Event event2 = new TestEventSubclass();
		delegateMock.handleEvent(event2);
		replay(delegateMock);
		//TEST
		filter.valueChanged(selectionEvent);
		filter.handleEvent(event1);
		filter.handleEvent(event2);
		//VERIFY
		verify(delegateMock);
	}
	
	@Test
	public void testFilterForMultipleClasses() {
		//SETUP
		TreeSelectionEvent selectionEvent = createNiceMock(TreeSelectionEvent.class);
		TreePath[] treePath = createTreePathArray(TestEventSubclass.class,TestEvent.class);
		expect(selectionEvent.getPaths()).andReturn(treePath);
		expect(selectionEvent.isAddedPath(treePath[0])).andReturn(true);
		expect(selectionEvent.isAddedPath(treePath[1])).andReturn(true);
		replay(selectionEvent);
		Event event1 = new TestEvent();
		Event event2 = new TestEventSubclass();
		delegateMock.handleEvent(event1);
		delegateMock.handleEvent(event2);
		replay(delegateMock);
		//TEST
		filter.valueChanged(selectionEvent);
		filter.handleEvent(event1);
		filter.handleEvent(event2);
		//VERIFY
		verify(delegateMock);
	}
	
	public class TestEvent extends Event {
		public TestEvent() {
			super("Test");
		}
	}
	
	public class TestEventSubclass extends TestEvent {};
	
	private TreePath[] createTreePathArray(Class<?>... cls) {
		TreePath[] paths = new TreePath[cls.length];
		for (int i = 0; i<cls.length; i++) {
			paths[i] = new TreePath(new ClassTreeNode(cls[i]));
		}
		return paths;
	}
}
