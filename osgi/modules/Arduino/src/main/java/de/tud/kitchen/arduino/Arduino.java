package de.tud.kitchen.arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


/**
 * Represents the Arduino with its door sensors
 * it is implemented after the singleton design pattern so
 * that you are not able to instantiate more than one Arduino
 * at the same time
 * 
 * @author cklos
 *
 */
public class Arduino implements SerialPortEventListener{
	/**
	 * the Port of the device, its only changeable in the connect method
	 */
	private String port = "/dev/ttyS33";
	
	/**
	 * when we are connected, the connection will be on this serialPort
	 */
	private SerialPort serialPort;
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	// TODO: Maybe change to int
	public static final boolean LOW = false;
	public static final boolean HIGH = true;
	/**
	 * This Mode sends only sensor data if you request them
	 */
	public static final int MPOLL = '0';
	/**
	 * This Mode sends sensor data when any sensor changes
	 */
	public static final int MCHANGES = '1';
	/**
	 * This Mode sends always sensor data as fast as possible
	 */
	public static final int MAGRESSIVE = '2';
	
	/**
	 * Char representations for the ports so that you can use them extern
	 */
	public static final int SENSORA = 'a';
	public static final int SENSORB = 'b';
	public static final int SENSORC = 'c';
	public static final int SENSORD = 'd';
	public static final int SENSORE = 'e';
	
	/**
	 * stores the SensorEventListener
	 */
	private List<SensorEventListener> sensorEventListener = 
			new ArrayList<SensorEventListener>(); 
	
	/**
	 * stores the current mode
	 */
	private int mode = 0;
	
	/**
	 * the default static instanciation, the empty constructor
	 * and the getInstance method are part of the singleton pattern
	 */
	private static Arduino instance = new Arduino();
	
	private Arduino() {};
	
	/**
	 * gives you the instance you have to work with
	 * @return instance of the Arduino board
	 */
	public static Arduino getInstance() {
		return instance;
	}
	
	/**
	 * get connection state
	 * @return if the connection is established
	 */
	public boolean isConnected() {
		return serialPort != null;
	}
	
	/**
	 * establish a connection at the given Port
	 * @param port for example <br>
	 * <table> 
	 * 	<tr><td>"/dev/tty.usbserial-A9007UX1" 	</td><td>	on Mac 		</td></tr>
	 * 	<tr><td>"/dev/ttyS33"					</td><td>	on Linux	</td></tr>
	 * 	<tr><td>"COM3"							</td><td>	on Windows	</td></tr>
	 * </table>
	 * @return if it's possible to get a connection
	 */
	public boolean connect(String port){
		this.port = port;
		return this.connect();
	}
	
	/**
	 * establish a connection
	 * @return if it's possible to get a connection
	 */
	public boolean connect() {		
		// find the port with the given portname
		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
				if (currPortId.getName().equals(port)) {
					portId = currPortId;
					break;
				}			
		}		
		if (portId == null) {
			return false;
		}
		//connect
		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);
			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			// open the streams
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();
			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			return true;
		} catch (Exception e) {
			System.err.println(e.toString());
			return false;
		}
		
	}
	
	
	/**
	 * disconnects the port
	 * @return if it's able to find a connected port and disconnect it 
	 */
	public boolean disconnect() {
		if (this.isConnected()) {
			serialPort.removeEventListener();
			serialPort.close();
			serialPort = null;
			return true;
		}
		return false;		
	}
	
	/**
	 * switches the Arduino to the given mode
	 * @param Mode can be {@link Arduino.MPoll}, {@link Arduino.MChanges} 
	 * or {@link Arduino.MAgressive}
	 * @return success of sending the command
	 */
	public boolean switchMode(int Mode){
		if (this.isConnected()){
			try {
				output.write(Mode);
				return true;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * gets the status of the given Port its only available in the Mode MPoll
	 * @param Port
	 * @return
	 * @throws WrongModeSelected
	 */
	public boolean getValue(int Port) throws WrongModeSelected{
		try {
			output.write(Port);
			if (mode==0){
				int letter = input.read();
				// read bytes until we get the correct port data
				while (Character.toUpperCase(letter) != Character.toUpperCase(Port)){
					letter = input.read();
				}
				if (letter>=97) return Arduino.LOW; else return Arduino.HIGH;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new WrongModeSelected();
		
	}

	@Override
	public void serialEvent(SerialPortEvent SPE) {
		if (SPE.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				// get next byte
				int inputByte = input.read();
				if (inputByte == -1) return;
				// transform it to lowercase
				Character lowerCaseInputByte = 
						Character.toLowerCase((char) inputByte);
				// create the event
				DoorSensorEvent triggeredEvent;
				if (lowerCaseInputByte>='a' && lowerCaseInputByte<='e'){
					triggeredEvent = new DoorSensorEvent(SPE,
							lowerCaseInputByte, 
							((inputByte>='a' && inputByte<='e') ? Arduino.LOW : Arduino.HIGH));
				} else 
					// it its not sensor data check if the board tells you something about a mode change
					// and store the new mode or return becouse that was no valid data
					if (inputByte>=48 && inputByte<=50) {
						this.mode = inputByte-48;
						return;
					} else return;
				
				CallSensorEventListener[] threads = 
						new CallSensorEventListener[sensorEventListener.size()];
				for (int i = 0; i<sensorEventListener.size(); i++){
					threads[i] = 
							new CallSensorEventListener(sensorEventListener.get(i),
									triggeredEvent);
					threads[i].run();
				}
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		
	}
	
	
	/**
	 * add an SensorEventlistener to the Arduino
	 * it's always called when new Sensor is coming via the serial port
	 * @param sEL SensorEvenentListener that should be added
	 * @return if it was able to add the listener
	 */
	public boolean addSensorEventListener(SensorEventListener sEL){
		
			sensorEventListener.add(sEL);
			return true;
		
	}
	
	
}

/**
 * this class is used when we want to tell the SensorEventListeners
 * that Sensor data arrived.
 * @author cklos
 *
 */
class CallSensorEventListener extends Thread {
	
	private SensorEventListener sEL;
	private DoorSensorEvent sE;
	
	public CallSensorEventListener(SensorEventListener sEL, DoorSensorEvent sE) {
		this.sE = sE;
		this.sEL = sEL;
	}
	
	@Override
	public void run() {
		sEL.SensorEvent(sE);
		super.run();
	}
}