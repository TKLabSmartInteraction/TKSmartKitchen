package de.tud.kitchen.main.impl.events;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MainDispatcher implements Runnable {


	private final ThreadGroup subDispatchThreadGroup;
	private final ExecutorService threadPool;
	private final BlockingQueue<DispatchRequest> dispatchRequests;
	private transient boolean running = true;
	private final RessourceManager manager;
	

	public MainDispatcher(RessourceManager manager, ThreadGroup threadGroup) {
		subDispatchThreadGroup = new ThreadGroup(threadGroup, "Dispatch Threads");
		threadPool = Executors.newCachedThreadPool(
		new ThreadFactory() {
			private AtomicInteger threadCounter;
			@Override
			public Thread newThread(Runnable arg0) {
				return new Thread(subDispatchThreadGroup, "Dispatch Thread #" + threadCounter.getAndIncrement());
			}
		});
		this.manager = manager;
		dispatchRequests = new LinkedBlockingQueue<DispatchRequest>();
	}
	
	public void scheduleDispatch(DispatchRequest request) {
		dispatchRequests.offer(request);
	}

	@Override
	public void run() {
		while(running) {
			try {
				final DispatchRequest request = dispatchRequests.poll(100, TimeUnit.MILLISECONDS);
				request.dispatch(threadPool);
				manager.returnDispatchRequest(request);
			} catch (InterruptedException e) {}
		}
	}
}
