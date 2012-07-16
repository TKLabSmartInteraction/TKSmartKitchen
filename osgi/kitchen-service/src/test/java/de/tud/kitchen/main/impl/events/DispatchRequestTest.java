package de.tud.kitchen.main.impl.events;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;

import java.util.LinkedList;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.Test;

public class DispatchRequestTest {

	
	private LinkedList<DispatchCallable> preparedCallables;
	private DispatchRequest request;
	
	@Before
	public void before() {
		request = new DispatchRequest(createNiceMock(RessourceManager.class));
		preparedCallables = new LinkedList<DispatchCallable>();
		for(int i = 1; i < 5; i++) {
			DispatchCallable callable = createMock(DispatchCallable.class);
			try {
				callable.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			expectLastCall().andReturn(null).times(1);
			preparedCallables.add(callable);
			replay(callable);
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testInitStateDispatch() {
		//TEST
		request.dispatch(null);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testDoubleInit() {
		//TEST
		request.setDispatchData(preparedCallables);
		request.setDispatchData(preparedCallables);
	}
	
	@Test
	public void testInitStateSetDataAndDispatch() {
		//TEST
		request.setDispatchData(preparedCallables);
		request.dispatch(Executors.newSingleThreadExecutor());
		//VERIFY
		verify(preparedCallables.toArray(new Object[preparedCallables.size()]));
	}
	

	
	@Test(expected=IllegalStateException.class)
	public void testRanStateAndDispatch() {
		testInitStateSetDataAndDispatch();
		//TEST
		request.dispatch(Executors.newSingleThreadExecutor());
		//VERIFY
	}
	
	@Test
	public void testReuse() {
		testInitStateSetDataAndDispatch();
		//SETUP
		for (DispatchCallable callable : preparedCallables) {
			reset(callable);
			try {
				callable.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			expectLastCall().andReturn(null).times(1);
			replay(callable);
		}
		//TEST
		request.setDispatchData(preparedCallables);
		request.dispatch(Executors.newSingleThreadExecutor());
		//VERIFY
		verify(preparedCallables.toArray(new Object[preparedCallables.size()]));
	}
	
	@Test(timeout=200)
	public void testWatchdog() {
		testInitStateSetDataAndDispatch();
		//SETUP
		for (DispatchCallable callable : preparedCallables) {
			reset(callable);
			try {
				callable.call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			expectLastCall().andReturn(null).times(1);
			replay(callable);
		}
		DispatchCallable hangingCallable = new DispatchCallable() {
			@Override
			public Void call() throws Exception {
				while (true) {
					Thread.sleep(1000);
				}
			}
		};
		preparedCallables.add(hangingCallable);
		//TEST
		request.setDispatchData(preparedCallables);
		request.dispatch(Executors.newSingleThreadExecutor());
	}
}
