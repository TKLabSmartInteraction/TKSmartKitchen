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

package de.tud.kitchen.main.impl.events;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;


public class DispatchManager {

	private final ThreadGroup dispatchThreadGroup;
	private final Thread dispatchThread;
	private final RessourceManager ressourceManager;
	private final MainDispatcher mainDispatcher;
	
	public DispatchManager() {
		dispatchThreadGroup = new ThreadGroup("Main Dispatch");
		ressourceManager = new RessourceManager();
		mainDispatcher = new MainDispatcher(ressourceManager, dispatchThreadGroup);
		dispatchThread = new Thread(dispatchThreadGroup,mainDispatcher,"Main Dispatch Thread");
		dispatchThread.setDaemon(true);
	}
	
	public void start() {
		dispatchThread.start();
	}
	
	public void stop() {
		mainDispatcher.shutdown();
	}
	
	public void scheduleDispatch(Collection<EventConsumer> consumer, Event event) {
		List<DispatchCallable> callables = ressourceManager.getFreeCallables(consumer.size());
		Iterator<DispatchCallable> callableIterator = callables.iterator();
		Iterator<EventConsumer> eventConsumerIterator = consumer.iterator();
		while(callableIterator.hasNext() || eventConsumerIterator.hasNext()) {
			callableIterator.next().setData(eventConsumerIterator.next(), event);
		}
		DispatchRequest request = ressourceManager.getFreeDispatchRequest();
		request.setDispatchData(callables);
		mainDispatcher.scheduleDispatch(request);
	}
	
}
