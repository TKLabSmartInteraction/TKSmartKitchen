package de.tud.kitchen.serialprovider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TooManyListenersException;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.RXTXPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;


public class ControlledPort {

	public ControlledPort(String portname){
		this.portname = portname;
		//System.setErr(null); // surpress RXTX JNI warnings
	}
	
	private String endpointPortname = "Defaultname";		// stores portname configured by SPServiceEndpoint
	private String portname;								// stores real portname
	private SerialPortBinding binding = null;
	private ArrayList<DeviceIdentifier> nonMatchingIdentifiers = new ArrayList<DeviceIdentifier>();
	private RXTXPort port = null;
	private CommPortIdentifier portIdentifier = null;
	private InputStream rxtxInstream;
	private OutputStream rxtxOutstream;
	private DataInputStream instream;
	private DataOutputStream outstream;
	
	public String getPortname(){
		return portname;
	}
	
	public String getEndpointPortname(){
		return endpointPortname;
	}
	
	public void addSPEListener(SerialPortEventListener speListener) throws TooManyListenersException{
		if(port instanceof RXTXPort){
			try {
				port.addEventListener(speListener);
				port.notifyOnDataAvailable(true);
			} catch (TooManyListenersException e) {
				throw e;
			}
		}
	}
	
	public void setBinding(SerialPortBinding binding){
		if(devicePresent()){
			this.binding = binding; 		// set binding
			//System.out.println("Set binding for port: " + portname);
			try {
				connectSerial();			// establish serial connection
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			
			this.binding.getEndpoint().serialDeviceAttached(instream, outstream, new SPSerialPortFactoryWorker());	// serve streams to SPServiceEndpoint
		}
	}
	
	public void deleteBinding(){
		if(isBound()){
			binding.getEndpoint().serialDeviceRemoved(instream, outstream, new SPSerialPortFactoryWorker()); 			// notifiy SPServiceEndpoint //TODO: blocking - add timeout / streamcopy?
			binding = null;					// delete binding
			
			if(deviceReadyForDisconnect()){
				try {
					disconnectSerial();
				} catch (IOException e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	public boolean isBound(){
		return (this.binding instanceof SerialPortBinding);
	}
	
	private boolean isNewIdentifier(DeviceIdentifier identifier){
		for(DeviceIdentifier nonMatchingIdentifier: nonMatchingIdentifiers){
			if(nonMatchingIdentifier.equals(identifier))
				return false;
		}
		return true;
	}
	
	private void addIdentifier(DeviceIdentifier identifier){
		nonMatchingIdentifiers.add(identifier);
	}
	
	public boolean isMatching(DeviceIdentifier identifier){
		if(!isBound() && isNewIdentifier(identifier)){	// if no binding, device present and not already checked against identifier 
			boolean match;													// -> perform device indentification		
			addIdentifier(identifier);										// update list of non matching device identifiers
			//System.out.println("Serial provider: Trying to match device " + portname);
			try { 
				connectSerial();
				match = identifier.deviceIdentification(instream, outstream);
				disconnectSerial();
			} catch (IOException e) {
				//System.out.println("Serial provider: Device match failed (" + e.getMessage() + ")");
				return false;
			} 
			
			/*if(match == false)
				System.out.println("Serial provider: Device match failed (Device " + portname + " did not identify)");*/
			return match;
		}
		else {
			return false; // device already bound to endpoint -> match neither needed nor possible
		}
		
	}
	
	private boolean deviceReadyForDisconnect(){ 					// Failsafe
		return (devicePresent() && deviceConnected());
	}
	
	private boolean devicePresent(){
		return (portIdentifier instanceof CommPortIdentifier);
	}
	
	private boolean deviceConnected(){
		return (port instanceof RXTXPort);
	}
	
	private void connectSerial() throws IOException{
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portname);
		} catch (NoSuchPortException e) {
			portIdentifier = null;
			throw new IOException("Opening port failed. No such port!");
		}
		try {
			port = portIdentifier.open("NRSerialPort", 2000); 		// TEST: RXTXPort instead of NRJavaSerialPort -> more fine granular control & event driven
		} catch (PortInUseException e) {
			port = null;
			throw new IOException("Opening port failed. Port in use!");
		} 
		
		try {
			port.setSerialPortParams(9600, RXTXPort.DATABITS_8, RXTXPort.STOPBITS_1, RXTXPort.PARITY_NONE); // connect at 9600 baud in first approach
			port.enableReceiveTimeout(100);
		} catch (UnsupportedCommOperationException e) {
			port = null;
			throw new IOException("Configuring port failed. Unsupported operation!");
		}
		
		rxtxInstream = port.getInputStream();		
		rxtxOutstream = port.getOutputStream();		
		instream = new DataInputStream(rxtxInstream);		// prepare data input stream
		outstream = new DataOutputStream(rxtxOutstream);	// prepare data output stream
		
		//System.out.println("Port " + portname + " opened.");
	}
	
	private void disconnectSerial() throws IOException{
		//System.out.println("Disconnecting serial port - Step 1.");
		try {
			rxtxInstream.close();
			//System.out.println("Disconnecting serial port - Step 2.");
			rxtxOutstream.close();
			//System.out.println("Disconnecting serial port - Step 3.");
			//System.out.println("All streams used by port " + portname + " closed.");
		} catch (IOException e) {
			throw new IOException("Closing streams failed. I/O-exception!");
		}
		try {
			port.close();
			//System.out.println("Disconnecting serial port - Step 6.");
			port = null;
			//System.out.println("Disconnecting serial port - Step 7.");
			//System.out.println("Port " + portname + " closed and destroyed.");
		} catch (NullPointerException e) {
			throw new IOException("Closing device failed. Device not existing!");
		}
		
	}
	

	private class SPSerialPortFactoryWorker implements SPSerialPort {
		
		@Override
		public String getDeviceName() {
			return portname;
		}
		
		@Override
		public int getRequestHash() {
			return binding.getRequestHash();
		}
		
		@Override
		public void addSerialPortEventListener (
				SerialPortEventListener speListener) throws TooManyListenersException {
			try {
				addSPEListener(speListener);
			} catch (TooManyListenersException e) {
				throw e;
			}
		}
		
		@Override
		public String getPortnameAlias() {
			return endpointPortname;
		}
		
		@Override
		public void setPortnameAlias(String alias) {
			endpointPortname = alias;
		}
	}

	
	
}	