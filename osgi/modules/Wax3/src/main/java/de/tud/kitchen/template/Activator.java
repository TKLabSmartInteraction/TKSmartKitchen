package de.tud.kitchen.template;

import java.io.IOException;
import java.net.SocketException;
import java.util.Date;

import javax.swing.JOptionPane;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

/**
 * Starts the listening for the Wax3 sensors
 * @author Christian Klos
 *
 */
public class Activator extends KitchenModuleActivator {
	
	
	/**
	 * the data is comming via OSC so just create a Port
	 */
	OSCPortIn port;
	EventPublisher<AccelerometerEvent> publisher;

	@Override
	public void start(Kitchen kitchen) {
		/**
		 * when running under windows promt for Port and direct start the Waxrec
		 */
		if (System.getProperty("os.name").toLowerCase().contains("windows")){
			System.out.println("running on Windows");
			String comPort = JOptionPane.showInputDialog(
					"Please insert the Port where the WAX3 receiver is Plugged in", "COM3");
			try {
				Runtime.getRuntime().exec(".\\waxrec.exe \\\\.\\"+comPort+" -osc localhost:57110 -timetag");
			} catch (IOException e) {
				System.out.println("Failed to launch Waxrec");
			}
		}
		
		/**
		 * start listening on the Wax3 sensors 101 - 104
		 */
		try {
			port = new OSCPortIn(57110);
			System.out.println("assigned Port");
			publisher = kitchen.getEventPublisher(AccelerometerEvent.class);
			port.addListener("/wax/101", new parametricOscListener("101"));
			port.addListener("/wax/102", new parametricOscListener("102"));
			port.addListener("/wax/103", new parametricOscListener("103"));
			port.addListener("/wax/104", new parametricOscListener("104"));
			port.startListening();
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void stop() {
		/**
		 * shutdown everything properly
		 */
		port.stopListening();
		port.close();
	}

	/**
	 * 
	 * @author Christian Klos
	 * 
	 * Listener on the OSC Port that transforms the OSC packets to
	 * Accelerometer Events and Publishes them
	 *
	 *
	 */
	private class parametricOscListener implements OSCListener {
		String source;
		private long lastTime;

		public parametricOscListener(String source) {
			super();
			this.source = source;
		}

		@Override
		public void acceptMessage(Date arg0, OSCMessage arg1) {
			/**
			 * you can access the values of the incomming OSC paket as an
			 * Object Array with:
			 * arg1[0] = x
			 * arg1[1] = y
			 * arg1[2] = z
			 * arg1[3] = counter
			 * arg1[4] = timetag
			 */
			long time = ((Date) arg1.getArguments()[4]).getTime();
			/**
			 * sometimes the timetag is just 0 so we set it manually
			 */
			if (time == 0) 
				time = lastTime+20;
			lastTime=time;
			/* Create the Event and publish it */
			final AccelerometerEvent<Float> event = new AccelerometerEvent<Float>(source,
					time, 
					(Float) arg1.getArguments()[0], 
					(Float) arg1.getArguments()[1],
					(Float) arg1.getArguments()[2]);
			publisher.publish(event);
		}

	}
}