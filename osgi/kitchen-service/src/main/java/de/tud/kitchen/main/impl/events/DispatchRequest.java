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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DispatchRequest {
	
	//0: init
	//1: prepared
	//2: running
	//3: ran
	private final AtomicInteger state = new AtomicInteger(0);
	private final CopyOnWriteArrayList<DispatchCallable> callable = new CopyOnWriteArrayList<DispatchCallable>();
	private final RessourceManager manager;
	
	public DispatchRequest(RessourceManager manager) {
		this.manager = manager;
	}
	
	
	void setDispatchData(List<DispatchCallable> list) {
		if (state.compareAndSet(0, 1) || state.compareAndSet(3, 1)) {
			callable.clear();
			callable.addAll(list);
		} else {
			throw new IllegalStateException("new data can only be set while the Dispatch is not prepared and did not ran");
		}
	}
	
	void dispatch(ExecutorService services) {
		if (state.compareAndSet(1, 2)) {
			try {
				for (Future<Void> result : services.invokeAll(callable,100,TimeUnit.MILLISECONDS)) {
						if (result.isCancelled()) {
								System.err.println(result + " failed to dispatch");
						}
						else {
							try {
								result.get();
							} catch (ExecutionException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {}
						}
							
				}
				manager.returnCallables(callable);
				state.set(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		else 
			throw new IllegalStateException("This dispatch request is not ready to be processed");
	}
}
