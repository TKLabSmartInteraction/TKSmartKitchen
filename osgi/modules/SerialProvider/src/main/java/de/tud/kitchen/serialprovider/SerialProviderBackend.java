package de.tud.kitchen.serialprovider;

import gnu.io.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.lang.Thread;
import java.lang.Runnable;

public class SerialProviderBackend implements Runnable{

	private static SerialProviderBackend instance = new SerialProviderBackend();
	
	private SerialProviderBackend(){}
	
	public static SerialProviderBackend getInstance(){
		return instance;
	}
	
	public void start(){ 			//TODO: research in exact OSGi class loading behaviour: object persistent? -> clear pending request prior to start
		startDeviceController();	// start device controller thread
	}
	
	public void stop(){
		stopDeviceController();		// stop device controller thread
		//releaseAll();				// remove all port bindings (notifies endpoints -> provided streams are no longer served) //TODO: synchronize corresponding blocks
	}
	
	private void startDeviceController(){
		deviceController = new Thread(this,"DeviceController Thread");
		deviceController.start();
	}
	
	private void stopDeviceController(){
		deviceController.interrupt();
	}
	
	public boolean deviceControllerStatus(){
		return deviceController.isAlive();
	}
	
	public void run(){
		try {
			while(!Thread.currentThread().isInterrupted()){
				/* device explorer */
				String[] availablePortnames = enumerateAvailablePortnames(); // get listing of portnames currently available
				/* check if new ports are not yet controlled */
				for(String portname: availablePortnames){ // check for ports that are not yet controlled
					if(!isControlled(portname)){
						gainControl(portname); // add ports that are not yet controlled
					} 
				}
				/* check if controlled ports are no longer available */
				ArrayList<ControlledPort> controlledPortscopy = new ArrayList<ControlledPort>(controlledPorts);
				for(ControlledPort port: controlledPortscopy){ // check for controlled ports that are not available anymore
					if(!isStringContained(port.getPortname(), availablePortnames)){
						removeControl(port.getPortname()); // remove ports that are no longer available
					}
				}
				/**/
				/* device controller */
				/* process pending requests */
				ArrayList<DeviceRequest> pendingRequestscopy = new ArrayList<DeviceRequest>(pendingRequests);
				for(ControlledPort port: controlledPorts){
					for(DeviceRequest request: pendingRequestscopy){
						if(!request.isDisableFlag() && port.isMatching(request.getIdentifier())){
							//System.out.println("Serial provider: Device " + port.getPortname() + " matching request #:" + request.hashCode());
							port.setBinding(new SerialPortBinding(request.getEndpoint(), request.hashCode())); // set port binding (serves SPService)
							request.setDisabled();
							pendingRequests.remove(request);// remove request from list of pending requests
						}
					}
				}
				/**/
				Thread.sleep(2000);
			}
		//} catch (InterruptedException e) {
		  } catch (Exception e) {
			  deviceController.interrupt(); // call interrupt again
			  this.lastE = e;
		  } catch (Error e) {
			  this.lastE = e;
		  }
	}
	
	private Throwable lastE;
	private Thread deviceController;
	private ArrayList<ControlledPort> controlledPorts = new ArrayList<ControlledPort>();
	private ArrayList<DeviceRequest> pendingRequests = new ArrayList<DeviceRequest>();
	private enum Platform {
		Windows,
		Mac,
		Unix,
		Linux,
		Unknown
	}
	
	private Platform getOS(){
		String OSProperty = System.getProperty("os.name").toLowerCase();
		if(OSProperty.indexOf("win") != -1){
			return Platform.Windows;
		}
		else if (OSProperty.indexOf("nux") != -1){
			return Platform.Linux;
		}
		else if (OSProperty.indexOf("nix") != -1){	
			return Platform.Unix;
		}
		else if (OSProperty.indexOf("mac") != -1){	
			return Platform.Mac;
		}
		else {
			return Platform.Unknown;
		}
	}
	
	public String[] enumerateAvailablePortnames(){
		if(getOS() == Platform.Linux || getOS() == Platform.Unix || getOS() == Platform.Mac){
			return enumeratePorts("tty");
		}
		else if(getOS() == Platform.Windows){
			return enumeratePorts("com");
		}
		else{
			return enumeratePorts();
		}
	}
	
	private String[] enumeratePorts(){
		return enumeratePorts("");
	}
	
	@SuppressWarnings("rawtypes")
	private String[] enumeratePorts(String filterPortname) { //TODO: add parameterization of raw-types
	    Enumeration ports = CommPortIdentifier.getPortIdentifiers();
	    ArrayList<String> portList = new ArrayList<String>();
	    String portArray[] = null;
	    while (ports.hasMoreElements()) {
	        CommPortIdentifier port = (CommPortIdentifier) ports.nextElement();
	        if (port.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	            if(filterPortname.length() == 0){
	            	portList.add(port.getName());
	            }
	            else if(port.getName().toLowerCase().indexOf(filterPortname.toLowerCase()) != -1){
	            	portList.add(port.getName());
	            }
	        }
	    }
	    portArray = (String[]) portList.toArray(new String[0]);
	    return portArray;
	}
	
	private void gainControl(String portname){
		controlledPorts.add(new ControlledPort(portname));
		deviceAttached(portname);
	}
	
	private void removeControl(String portname){
		ArrayList<ControlledPort> copy = new ArrayList<ControlledPort>(this.controlledPorts);
		for(ControlledPort port: copy){
			if(port.getPortname().equals(portname)){
				port.deleteBinding();		  // delete binding (notifies SPServiceEndpoint)
				controlledPorts.remove(port); // delete port
			}
		}
		deviceRemoved(portname);
	}
	
	@SuppressWarnings("unused")
	private void releaseAll(){
		for(ControlledPort port: controlledPorts){
			port.deleteBinding();		  // delete binding (notifies SPServiceEndpoint)
			controlledPorts.remove(port); // delete port
		}
	}
	
	private boolean isControlled(String portname){
		for(ControlledPort port: controlledPorts){
			if(port.getPortname().equals(portname))
				return true;
		}
		return false;
	}
	
	private boolean isStringContained(String searchString, String[] stringArray){
		for(String compare: stringArray){
			if(compare.equals(searchString))
				return true;
		}
		return false;
	}
	
	private void deviceAttached(String portname){ // called if new device was found
		//System.out.println("Serial provider: Device " + portname + " attached.");
	}
	
	private void deviceRemoved(String portname){ // // called if existing device was removed
		//System.out.println("Serial provider: Device " + portname + " removed.");
	}
	
	public int requestDevice(DeviceIdentifier identifier, SPServiceEndpoint endpoint){
		DeviceRequest request = new DeviceRequest(identifier, endpoint);
		pendingRequests.add(request);
		return request.hashCode();
	}
	
	public void revokeDeviceRequest(int hash){
		ArrayList<DeviceRequest> copy = new ArrayList<DeviceRequest>(pendingRequests);
		for(DeviceRequest request: copy){
			if(request.hashCode() == hash)
				pendingRequests.remove(request);
		}
		System.out.println("Revocation completed.");
	}
	
	public ArrayList<DeviceRequest> getPendingRequests(){
		return pendingRequests;
	}
	
	public Throwable getLastError(){
		return lastE;
	}
	
}
