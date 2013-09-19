package de.tud.kitchen.serialprovider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Math;

public class ChallengeResponse implements DeviceIdentifier{

	public ChallengeResponse(byte[] challenge, byte[] response){
		this.challenge = challenge;
		this.response = response;
		this.waitForReady = 0;
		this.waitForResponse = 50;
	}
	
	public ChallengeResponse(byte[] challenge, byte[] response, int waitForReady, int waitForResponse){
		this.challenge = challenge;
		this.response = response;
		this.waitForReady = waitForReady;
		this.waitForResponse = waitForResponse; //TODO: implement this!
	}
	
	private final String humanReadableName = "Challenge-Response";
	private byte[] challenge;
	private byte[] response;
	private int waitForReady;
	private int waitForResponse;
	
	@Override
	public String getHumanReadableName() {
		return humanReadableName;
	}
	
	@Override
	public String getHumanReadableDeviceIdentification() {
		StringWriter out = new StringWriter();
		PrintWriter outWriter = new PrintWriter(out);
		outWriter.append("Sending: ");
		
		for(byte send : challenge){
			outWriter.append(convertToHex(send) + " ");
		}
		
		outWriter.append("Expecting: ");
		
		for(byte expected : response){
			outWriter.append(convertToHex(expected) + " ");
		}
		
		out.append("wReady: " + waitForReady + "ms wResponse: " + waitForResponse + "ms");
		return out.toString();
	}
	
	private String convertToHex(byte Byte){
		StringBuilder sb = new StringBuilder();
	    sb.append(String.format("%02X ", Byte));
	    return "0x".concat(sb.toString());
	}
	
	@Override
	public boolean deviceIdentification(DataInputStream instream,
			DataOutputStream outstream) throws IOException{
		
		try {
			Thread.sleep(waitForReady); // wait for serial device to become ready
		} catch (InterruptedException e) {
		}
		
		try {
			instream.skipBytes(instream.available()); // delete all bytes in device buffer
			int overlap = Math.min(challenge.length,response.length);
			if(overlap == 0)
				return false;
			for(int i=0; i<overlap;i++){
				outstream.writeByte(challenge[i]);
				//System.out.println("Wrote challenge byte to device: " + challenge[i]);
				try {
					Thread.sleep(waitForResponse); // wait for serial device to become ready
				} catch (InterruptedException e) {
				}
				byte read;
				try {
					if(instream.available() >= 1){ // RXTXPort vs NRSerialPort: read does -really- block!
						read = instream.readByte();
					}
					else {
						read = 0;
					}
				} catch (IOException e) {
					throw new IOException("CR: I/O-exception!");
				}
				//System.out.println("Read response byte from device: " + read);
				if(read != response[i]){
					return false;
				}
			}
			return true;
		} catch (IOException e) {
			outstream.flush();
			return false;
		}
	}
}
