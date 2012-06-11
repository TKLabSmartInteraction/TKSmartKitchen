package de.tud.kitchen.android;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorEventReceiver implements SensorEventListener {
	private Sensor mAccelerometer;
	private SensorManager mSensorManager;

	private SensorData current_data;

	private AccelerometerSender sender_instance;

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

	public void startSimulation() {
		if (mAccelerometer != null) {
			mSensorManager.registerListener(this, mAccelerometer, AccelerometerSender.SENSOR_DELAY);
		}
	}

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
