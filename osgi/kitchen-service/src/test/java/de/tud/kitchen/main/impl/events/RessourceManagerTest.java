/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.main.impl.events;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RessourceManagerTest {
	
	
	RessourceManager manager;
	
	@Before
	public void before() {
		manager = new RessourceManager();
	}
	
	@Test
	public void testGetVariousNumbersOfCallables() {
		//TEST
		List<DispatchCallable> threeCallables = manager.getFreeCallables(3);
		List<DispatchCallable> fiveCallables = manager.getFreeCallables(5);
		List<DispatchCallable> tenCallables = manager.getFreeCallables(10);
		//VERIFY
		assertEquals(3, threeCallables.size());
		assertEquals(5, fiveCallables.size());
		assertEquals(10, tenCallables.size());
		for (DispatchCallable dispatchCallable : threeCallables) {
			assertNotNull(dispatchCallable);
			assertFalse(fiveCallables.contains(dispatchCallable));
			assertFalse(tenCallables.contains(dispatchCallable));
		}
		for (DispatchCallable dispatchCallable : fiveCallables) {
			assertNotNull(dispatchCallable);
			assertFalse(tenCallables.contains(dispatchCallable));
		}
		for (DispatchCallable dispatchCallable : tenCallables) {
			assertNotNull(dispatchCallable);
		}
	}
	
	@Test
	public void testReuseCallables() {
		//TEST
		List<DispatchCallable> threeCallables = manager.getFreeCallables(3);
		manager.returnCallables(threeCallables);
		List<DispatchCallable> newThreeCallables = manager.getFreeCallables(3);
		//VERIFY
		assertEquals(3, threeCallables.size());
		assertEquals(3, newThreeCallables.size());
		for (DispatchCallable dispatchCallable : threeCallables) {
			assertNotNull(dispatchCallable);
			assertTrue(newThreeCallables.contains(dispatchCallable));
		}
		
	}
	
	@Test
	public void testGetDispatchRequest() {
		//TEST
		DispatchRequest firstRequest = manager.getFreeDispatchRequest();
		DispatchRequest secondRequest = manager.getFreeDispatchRequest();
		//VERIFY
		assertNotNull(firstRequest);
		assertNotNull(secondRequest);
		assertNotSame(firstRequest, secondRequest);
	}
	
	@Test
	public void testReuseDispatchRequest() {
		//TEST
		DispatchRequest firstRequest = manager.getFreeDispatchRequest();
		manager.returnDispatchRequest(firstRequest);
		DispatchRequest secondRequest = manager.getFreeDispatchRequest();
		//VERIFY
		assertNotNull(firstRequest);
		assertNotNull(secondRequest);
		assertSame(firstRequest, secondRequest);
	}
	
}