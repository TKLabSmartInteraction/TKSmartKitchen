/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 
 * @author Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 * 
 */
public class SensorEventReceiver implements SensorEventListener {
	private Sensor mAccelerometer;
	private SensorManager mSensorManager;

	private SensorData current_data;

	private AccelerometerSender sender_instance;

	/**
	 * Constructor for the class. Connects to the accelerometer if one is installed in the phone.
	 * 
	 * @param sender
	 *            The accelerometer app controlling the event receiver.
	 */
	public SensorEventReceiver(AccelerometerSender sender) {
		sender_instance = sender;
		mSensorManager = (SensorManager) sender.getSystemService(Activity.SENSOR_SERVICE);

		current_data = new SensorData();

		if ((mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)) == null) {
			sender_instance.setStatus("Error: No Accelerometer sensor found!");
		} else {
			startSimulation();
		}
	}

	/**
	 * Registers the event receiver as a listener to the android sensor manager.
	 */
	public void startSimulation() {
		if (mAccelerometer != null) {
			mSensorManager.registerListener(this, mAccelerometer, AccelerometerSender.SENSOR_DELAY);
		}
	}

	/**
	 * Unregisters the event receiver from the android sensor manager.
	 */
	public void stopSimulation() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			current_data.setData(event, SensorManager.GRAVITY_EARTH);

			sender_instance.transmitData(current_data);
		}
	}
}
