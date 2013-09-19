package de.tud.kitchen.serialprovider;

public class DeviceRequest {

	public DeviceRequest(DeviceIdentifier identifier, SPServiceEndpoint endpoint){
		this.identifier = identifier;
		this.endpoint = endpoint;
	}

	private DeviceIdentifier identifier;
	private SPServiceEndpoint endpoint;
	private boolean disableFlag = false;
	
	public DeviceIdentifier getIdentifier() {
		return identifier;
	}
	public SPServiceEndpoint getEndpoint() {
		return endpoint;
	}
	
	public boolean isDisableFlag() {
		return disableFlag;
	}
	public void setDisabled() {
		disableFlag = true;
	}
}

