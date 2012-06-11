package de.tud.kitchen.android;

import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class AccelerometerSender extends Activity {
	public final static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_UI;
	public final static String REMOTE_TYPE = "_tk_kitchen._tcp.local.";

	private SensorEventReceiver mSimulationView;

	private InetAddress IPAddress = null;

	private JmDNS jmdns;
	private ServiceListener listener;

	private TextView txtStatus;
	private boolean showData = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		txtStatus = (TextView) findViewById(R.id.txtStatus);
		setStatus("Starting. Not connected.");

		// Get an instance of the SensorManager
		mSimulationView = new SensorEventReceiver(this);

		// Get an IP server address using JmDNS.
		// Currently only one server is supported.
		try {
			jmdns = JmDNS.create();

			jmdns.addServiceListener(REMOTE_TYPE, listener = new ServiceListener() {
				public void serviceResolved(ServiceEvent ev) {
					IPAddress = ev.getInfo().getInetAddresses()[0];
					setStatus("Connected to: " + IPAddress);
				}

				public void serviceRemoved(ServiceEvent ev) {
					IPAddress = null;
					setStatus("Not connected!");
				}

				public void serviceAdded(ServiceEvent event) {
					// Required to force serviceResolved to be called again
					// (after the first search)
					jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
				}
			});
		} catch (Exception ex) {
			setStatus("Error: " + ex.getMessage());
		}
	}

	public void transmitData(SensorData data) {
		// If an IP address for a data receiver is set, transmit the sensor data.
		if (IPAddress != null) {
			try {
				OSCPortOut sender = new OSCPortOut(IPAddress);
				OSCMessage msg = new OSCMessage("/android/101", data.toOSCArray());
				sender.send(msg);
			} catch (Exception e) {
				setStatus("Send Error: " + e.getMessage());
			}
		}

		// If set by the user show the sensor data on the smartphone.
		if (showData) {
			EditText txtXAxes = (EditText) findViewById(R.id.outAccAxes0);
			txtXAxes.setText(String.format("%13.10f", data.getXAxes()));

			EditText txtYAxes = (EditText) findViewById(R.id.outAccAxes1);
			txtYAxes.setText(String.format("%13.10f", data.getYAxes()));

			EditText txtZAxes = (EditText) findViewById(R.id.outAccAxes2);
			txtZAxes.setText(String.format("%13.10f", data.getZAxes()));
		}
	}

	public void setStatus(String status) {
		txtStatus.setText(status);
	}

	public void doExit(View view) {
		mSimulationView.stopSimulation();
		this.finish();
	}

	public void setShowDataStatus(View view) {
		CheckBox chkShowData = (CheckBox) findViewById(R.id.chkShowData);
		showData = chkShowData.isChecked();
	}

}