package de.tud.kitchen.main.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModule;
import de.tud.kitchen.main.impl.IKitchenFactory;

public class KitchenModuleManagerTest {

	Kitchen kitchenMock;
	KitchenModule moduleMock;
	IKitchenFactory kitchenFactory;
	KitchenModuleManager manager;
	
	
	@Before
	public void before() {
		kitchenMock = createMock(Kitchen.class);
		moduleMock = createMock(KitchenModule.class);
		kitchenFactory = createMock(IKitchenFactory.class);
		expect(kitchenFactory.createKitchen(moduleMock)).andReturn(kitchenMock);
		replay(kitchenFactory);
		manager = new KitchenModuleManager(kitchenFactory);
	}
	
	
	@Test
	public void testAdd() {
		//SETUP
		moduleMock.start(kitchenMock);
		replay(moduleMock);
		//TEST
		manager.add(moduleMock);
		//VERIFY
		verify(moduleMock);
	}
	
	@Test
	public void testRemove() {
		//SETUP
		manager.add(moduleMock);
		reset(moduleMock);
		moduleMock.stop();
		replay(moduleMock);
		//TEST
		manager.remove(moduleMock);
		//VERIFY
		verify(moduleMock);
	}
	
}
