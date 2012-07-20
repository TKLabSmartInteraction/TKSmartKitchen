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
