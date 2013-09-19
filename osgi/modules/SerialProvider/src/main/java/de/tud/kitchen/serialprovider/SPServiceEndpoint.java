package de.tud.kitchen.serialprovider;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface SPServiceEndpoint {

	public void serialDeviceAttached(DataInputStream instream, DataOutputStream outstream, SPSerialPort port);
	public void serialDeviceRemoved(DataInputStream instream, DataOutputStream outstream, SPSerialPort port);
	
}
