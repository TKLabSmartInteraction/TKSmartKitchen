package de.tud.kitchen.main.impl;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;

import de.tud.kitchen.api.module.KitchenModule;

public class KitchenModuleManagerTest {

	KitchenInternal kitchenMock;
	KitchenModule moduleMock;
	IKitchenFactory kitchenFactory;
	KitchenModuleManager manager;
	
	
	@Before
	public void before() {
		kitchenMock = createMock(KitchenInternal.class);
		moduleMock = createMock(KitchenModule.class);
		kitchenFactory = createMock(IKitchenFactory.class);
		manager = new KitchenModuleManager(kitchenFactory);
	}
	
	
	@Test
	public void testAdd() {
		//SETUP
		expect(kitchenFactory.getSandboxedKitchen(moduleMock)).andReturn(kitchenMock);
		replay(kitchenFactory);
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
		expect(kitchenFactory.getSandboxedKitchen(moduleMock)).andReturn(kitchenMock).times(2);
		replay(kitchenFactory);
		manager.add(moduleMock);
		reset(moduleMock);
		moduleMock.stop();
		replay(moduleMock);
		reset(kitchenMock);
		kitchenMock.stop();
		replay(kitchenMock);
		//TEST
		manager.remove(moduleMock);
		//VERIFY
		verify(moduleMock);
		verify(kitchenFactory);
		verify(kitchenMock);
	}
	
}
