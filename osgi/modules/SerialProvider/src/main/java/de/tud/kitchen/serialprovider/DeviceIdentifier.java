package de.tud.kitchen.serialprovider;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface DeviceIdentifier {
	public boolean deviceIdentification(DataInputStream instream, DataOutputStream outstream) throws IOException;
	public String getHumanReadableName();
	public String getHumanReadableDeviceIdentification();
}
