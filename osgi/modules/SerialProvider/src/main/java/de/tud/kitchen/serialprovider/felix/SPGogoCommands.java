package de.tud.kitchen.serialprovider.felix;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.osgi.framework.BundleContext;
import org.apache.felix.service.command.Descriptor;

import de.tud.kitchen.serialprovider.DeviceRequest;
import de.tud.kitchen.serialprovider.SerialProviderBackend;

public class SPGogoCommands{

	public SPGogoCommands(BundleContext context){
		this.context = context;
		backendInstance = SerialProviderBackend.getInstance();
	}
	
	@SuppressWarnings("unused")
	private BundleContext context;
	private SerialProviderBackend backendInstance;
	
	@Descriptor("Lists all serial devices found.")
	public void listdevices(){
		System.out.println("Hello from Gogo Command!");
	}
	
	@Descriptor("Lists active serial port bindings.")
	public void listbindings(){
		System.out.println("Hello from Gogo Command 3!");
	}
	
	@Descriptor("Lists all pending device requests.")
	public void listpending(){
		boolean extensive = true;
		/*if(arg1.equals("-a"))
			extensive = true;*/
		
		ArrayList<DeviceRequest> pendingRequests = backendInstance.getPendingRequests();
		
		if(pendingRequests.size() > 0){
			StringWriter writer = new StringWriter();
			PrintWriter out = new PrintWriter(writer);
			
			for(DeviceRequest request: pendingRequests){
				if(!extensive){
					out.println("#" + request.hashCode() + " " + "Device-identifier: " + request.getIdentifier().getHumanReadableName());
				}
				else {
					out.println("#" + request.hashCode() + " " + "Device-identifier: " + request.getIdentifier().getHumanReadableName() + " Behaviour: " + request.getIdentifier().getHumanReadableDeviceIdentification());
				}
			}
			
			System.out.print(writer.getBuffer().toString());
		}
		else {
			System.out.println("No pending device requests.");
		}
	}
	
	public void status(){
		if(backendInstance.deviceControllerStatus()){
			System.out.println("Status: [OK] - Devicecontroller is running");
		} else {
			System.out.println("Status: [FAILURE] - Devicecontroller is not running");
		}
	}
	
	@Descriptor("Prints stacktrace.") //TODO: remove debugging code
	public void stacktrace(){
		if(backendInstance.getLastError() != null){
			System.out.print(getStackTraceAsString(backendInstance.getLastError()));
		}
		else {
			System.out.println("No error / exception saved.");
		}
		
	}
	
	private String getStackTraceAsString(Throwable t) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		t.printStackTrace(out);
		return writer.getBuffer().toString();
	}
	
}
