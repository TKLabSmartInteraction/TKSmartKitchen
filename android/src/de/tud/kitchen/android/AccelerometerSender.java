package de.tud.kitchen.android;

import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class AccelerometerSender extends Activity {
	public final static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_UI;
	public final static String REMOTE_TYPE = "_tk_kitchen._tcp.local.";

	private static final String PREFS_NAME = "TkKitchenPrefsData";

	private SensorEventReceiver mSimulationView;

	private InetAddress IPAddress = null;
	private int IPPort = 3334;

	private JmDNS jmdns;
	private ServiceListener listener;

	private EditText txtStatus;
	private EditText edtUserName;
	private boolean showData = false;

	private String userName;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		userName = "user" + Math.ceil(Math.random() * 100);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		userName = settings.getString("userName", userName);
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		edtUserName.setText(userName);

		TextWatcher textWatcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void afterTextChanged(Editable arg0) {
				userName = edtUserName.getText().toString();
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		};
		edtUserName.addTextChangedListener(textWatcher);

		txtStatus = (EditText) findViewById(R.id.txtStatus);
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
					IPPort = ev.getInfo().getPort();
					setStatus("Connected to: " + IPAddress + ":" + IPPort);
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
				OSCPortOut sender = new OSCPortOut(IPAddress, IPPort);
				OSCMessage msg = new OSCMessage("/android/" + userName, data.toOSCArray());
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

	@Override
	protected void onStop() {
		super.onStop();

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userName", userName);

		editor.commit();
	}
}