package de.tud.kitchen.serialprovider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ChallengeResponseString implements DeviceIdentifier{

	public ChallengeResponseString(String challenge, String response){
		this.challenge = challenge;
		this.response = response;
		this.exactMatch = false;
		this.waitForReady = 0;
		this.waitForResponse = 50;
	}
	
	public ChallengeResponseString(String challenge, String response, boolean exactMatch){
		this.challenge = challenge;
		this.response = response;
		this.exactMatch = exactMatch;
		this.waitForReady = 0;
		this.waitForResponse = 50;
	}
	
	public ChallengeResponseString(String challenge, String response, boolean exactMatch, int waitForReady, int waitForResponse){
		this.challenge = challenge;
		this.response = response;
		this.exactMatch = exactMatch;
		this.waitForReady = waitForReady;
		this.waitForResponse = waitForResponse; //TODO: implement this!
	}
	
	private final String humanReadableName = "Challenge-Response-String";
	private String challenge;
	private String response;
	private boolean exactMatch;
	private int waitForReady;
	private int waitForResponse;
	
	@Override
	public String getHumanReadableName() {
		return humanReadableName;
	}
	
	@Override
	public String getHumanReadableDeviceIdentification() {
		return "Sending: \"" + challenge + "\" Expecting: \"" + response + "\"" + "ExactMatch: " + exactMatch + "wReady: " + waitForReady + "ms wResponse:" + waitForResponse + "ms"; 
	}
	
	@Override
	public boolean deviceIdentification(DataInputStream instream,
			DataOutputStream outstream) throws IOException{
		byte[] buffer = new byte[100]; //TODO: use constant
		Arrays.fill(buffer, (byte)0);
		
		try {
			Thread.sleep(waitForReady); // wait for serial device to become ready
		} catch (InterruptedException e) {
		}
		
		try {
			instream.skipBytes(instream.available()); // delete all bytes in device buffer
			outstream.writeBytes(challenge);
			//System.out.println("Sent <--" + challenge + "--> to device.");
			try {
				Thread.sleep(waitForResponse); // wait for serial device to become ready
			} catch (InterruptedException e) {
			}
			if(instream.available() >= response.length()) // RXTXPort vs NRSerialPort: read does -really- block!
				instream.read(buffer,0,100);
		} catch (IOException e) {
			throw new IOException("CR-String: I/O-exception!");
		}
		String deviceResponse = new String(buffer);
		//System.out.println("Read <--" + deviceResponse + "--> from device.");
		if(!exactMatch){
			if(deviceResponse.indexOf(response) != -1)
				return true;
		}
		else {
			if(deviceResponse.equals(response))
				return true;
		}
		return false;
	}
}
