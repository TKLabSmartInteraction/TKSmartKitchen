package de.tud.kitchen.main.impl.events;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

class RessourceManager {
	private final LinkedBlockingQueue<DispatchCallable> callablePool;
	private final LinkedBlockingQueue<DispatchRequest> requestPool;

	
	public RessourceManager() {
		callablePool = new LinkedBlockingQueue<DispatchCallable>();
		requestPool = new LinkedBlockingQueue<DispatchRequest>();
	}
	
	public List<DispatchCallable> getFreeCallables(int num) {
		ArrayList<DispatchCallable> result = new ArrayList<DispatchCallable>(num);
		callablePool.drainTo(result, num);
		for (int i = result.size(); i<num; i++) {
			result.add(i, new DispatchCallable());
		}
		return result;
	}
	
	public void returnCallables (List<DispatchCallable> callables) {
		for (DispatchCallable callable: callables) {
			callable.reset();
		}
		callablePool.addAll(callables);
	}

	public DispatchRequest getFreeDispatchRequest() {
		DispatchRequest request = requestPool.poll();
		if (request != null) 
			return request;
		else 
			return new DispatchRequest(this);
	}

	public void returnDispatchRequest(DispatchRequest firstRequest) {
		requestPool.offer(firstRequest);
	}
}