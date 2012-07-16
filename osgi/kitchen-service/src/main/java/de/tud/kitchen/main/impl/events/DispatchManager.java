package de.tud.kitchen.main.impl.events;

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
		dispatchThread.start();
	}
	
	public void scheduleDispatch(List<EventConsumer> consumer, Event event) {
		List<DispatchCallable> callables = ressourceManager.getFreeCallables(consumer.size());
		for (int i = 0; i < consumer.size(); i++) {
			callables.get(i).setData(consumer.get(i), event);
		}
		DispatchRequest request = ressourceManager.getFreeDispatchRequest();
		request.setDispatchData(callables);
		mainDispatcher.scheduleDispatch(request);
	}
	
}
