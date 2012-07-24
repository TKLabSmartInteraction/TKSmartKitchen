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

package de.tud.kitchen.api.event;

import static org.easymock.EasyMock.*;
import org.junit.Test;

public class EventTest {
	
	@Test
	public void dispatchTest() {
		//SETUP
		EventConsumer eventConsumer = createMockBuilder(EventConsumer.class).addMockedMethod("handleObject").withConstructor().createMock();
		Event event = new Event("testSensor") {
			@Override
			protected String getAdditionalCsvHeader() {
				return "";
			}

			@Override
			protected String getAdditionalCsvValues() {
				return "";
			}
		};
		eventConsumer.handleObject(event);
		replay(eventConsumer);
		//TEST
		eventConsumer.handle(event);
		//CHECK
		verify(eventConsumer);
	}
}
