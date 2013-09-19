/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.arduino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;


/**
 * Represents the Arduino with its door sensors
 * it is implemented after the singleton design pattern so
 * that you are not able to instantiate more than one Arduino
 * at the same time
 * 
 * @author Christian Klos
 *
 */
public class Arduino implements SerialPortEventListener{
	
	/* port to SerialProvider */
	private DataInputStream input;
	private DataOutputStream output;
	/**/
	
	// TODO: Maybe change to int
	public static final boolean LOW = false;
	public static final boolean HIGH = true;
	/**
	 * This Mode sends only sensor data if you request them
	 */
//	public static final int MPOLL = '0';
	public static final int MPOLL = 0x30;
	/**
	 * This Mode sends sensor data when any sensor changes
	 */
//	public static final int MCHANGES = '1';
	public static final int MCHANGES = 0x31;
	/**
	 * This Mode sends always sensor data as fast as possible
	 */
//	public static final int MAGRESSIVE = '2';
	public static final int MAGRESSIVE = 0x32;
	
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
	 * the default static instanciation, the /empty/ constructor
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
	 * receive data streams from SerialProvider
	 * @param DataInputStream, DataOutputStream
	 */
	public void connect(DataInputStream instream, DataOutputStream outstream){
		this.input = instream;
		this.output = outstream;
	}
	
	
	/**
	 * switches the Arduino to the given mode
	 * @param Mode can be {@link Arduino.MPoll}, {@link Arduino.MChanges} 
	 * or {@link Arduino.MAgressive}
	 * @return success of sending the command
	 */
	public boolean switchMode(int Mode){
		try {
			System.out.println("Arduino: Switching mode to " + Mode);
			output.write(Mode);
			return true;
		} catch (IOException e) {
			return false;
		}
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
		System.out.println("Arduino: SPE received!");
		if (SPE.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				// get next byte
				int inputByte = input.read();
				System.out.println("Arduino: read input byte: " + inputByte);
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
 * @author Christian Klos
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