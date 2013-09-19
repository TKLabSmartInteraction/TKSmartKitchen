package de.tud.kitchen.serialprovider;

import de.tud.kitchen.serialprovider.SerialProviderBackend;

public class SerialProvider {

	private SerialProvider(){
		backendInstance = SerialProviderBackend.getInstance(); // get reference to SerialProviderBackend instance
	}
	
	public static SerialProvider getInstance(){
		return instance;
	}
	
	private static SerialProvider instance = new SerialProvider(); // static SerialProvider instantiation (singleton pattern)
	private SerialProviderBackend backendInstance; 
	
	public void requestDevice(DeviceIdentifier identifier, SPServiceEndpoint endpoint){
		backendInstance.requestDevice(identifier, endpoint);
	}
	
	public void revokeDeviceRequest(int hash){
		backendInstance.revokeDeviceRequest(hash);
	}
	
}
