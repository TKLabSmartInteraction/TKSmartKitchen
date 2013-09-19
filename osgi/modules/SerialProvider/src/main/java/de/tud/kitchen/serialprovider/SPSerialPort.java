package de.tud.kitchen.serialprovider;
import java.util.TooManyListenersException;

import gnu.io.SerialPortEventListener;

public interface SPSerialPort{
	public String getDeviceName();
	public int getRequestHash();
	public void setPortnameAlias(String alias);
	public String getPortnameAlias();
	public void addSerialPortEventListener(SerialPortEventListener speListener) throws TooManyListenersException;
}