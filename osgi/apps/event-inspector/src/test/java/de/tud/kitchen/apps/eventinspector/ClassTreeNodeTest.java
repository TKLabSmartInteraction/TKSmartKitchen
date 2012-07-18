package de.tud.kitchen.apps.eventinspector;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.apps.eventinspector.ClassTreeNode;

public class ClassTreeNodeTest {

	ClassTreeNode rootNode;
	
	@Before
	public void setupRootNode() {
		rootNode = new ClassTreeNode(Event.class);
		assertTrue(rootNode.getChildCount()==0);
	}
	
	@Test
	public void testToString() {
		//VERIFY
		assertEquals(rootNode.toString(), "Event");
	}
	
	
	@Test
	public void testFirstInsertionOfDirectSubclass() {
		//SETUP
		ClassTreeNode insertionNode = new ClassTreeNode(EventSubclass.class);
		//TEST
		rootNode.add(insertionNode);
		//VERIFY
		assertTrue(rootNode.getChildCount()==1);
		assertSame(insertionNode, rootNode.getChildAt(0));
	}
	
	@Test
	public void testSecondInsertionOfDirectSubclass() {
		//SETUP
		ClassTreeNode firstInsertionNode = new ClassTreeNode(EventSubclass.class);
		ClassTreeNode secondInsertionNode = new ClassTreeNode(EventSubclass.class);
		//TEST
		rootNode.add(firstInsertionNode);
		rootNode.add(secondInsertionNode);
		//VERIFY
		assertTrue(rootNode.getChildCount()==1);
		assertSame(firstInsertionNode, rootNode.getChildAt(0));
	}
	
	@Test
	public void testInsertionOfSecondOrderSubclassWithDirectSubclassAlreadyInserted() {
		//SETUP
		ClassTreeNode firstInsertionNode = new ClassTreeNode(EventSubclass.class);
		ClassTreeNode secondInsertionNode = new ClassTreeNode(SecondEventSubclass.class);
		rootNode.add(firstInsertionNode);
		//TEST
		rootNode.add(secondInsertionNode);
		//VERIFY
		assertTrue(rootNode.getChildCount()==1);
		assertTrue(firstInsertionNode.getChildCount()==1);
		assertSame(secondInsertionNode, firstInsertionNode.getChildAt(0));
	}
	
	@Test
	public void testDirectInsertionOfSecondOrderSubclass() {
		//SETUP
		ClassTreeNode secondInsertionNode = new ClassTreeNode(SecondEventSubclass.class);
		//TEST
		rootNode.add(secondInsertionNode);
		//VERIFY
		assertTrue(rootNode.getChildCount()==1);
		assertTrue(rootNode.getChildAt(0).getChildCount()==1);
		assertSame(secondInsertionNode, rootNode.getChildAt(0).getChildAt(0));
	}
	
	public class EventSubclass extends Event {
		public EventSubclass() {
			super("test");
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
	
	public class SecondEventSubclass extends EventSubclass {
		
	}
}
