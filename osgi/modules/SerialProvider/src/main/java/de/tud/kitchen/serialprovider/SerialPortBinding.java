package de.tud.kitchen.serialprovider;

import java.util.Date;

public class SerialPortBinding {

	public SerialPortBinding(SPServiceEndpoint endpoint, int requestHash){
		timestampCreated = new Date().getTime();
		this.endpoint = endpoint;
		this.requestHash = requestHash;
	}

	private long timestampCreated;
	private SPServiceEndpoint endpoint; 	// assigned endpoint
	private int requestHash;
	
	public long getTimestampCreated() {
		return timestampCreated;
	}
	public SPServiceEndpoint getEndpoint() {
		return endpoint;
	}
	
	public int getRequestHash() {
		return requestHash;
	}
}
