package de.tud.kitchen.android;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

public class AccelerometerSender extends Activity {

	public final String TAG = "Kitchen-DataSender";

	public final static int SENSOR_DELAY = SensorManager.SENSOR_DELAY_UI;
	public final static String REMOTE_TYPE = "_tk_kitchen._tcp.local.";

	private static final String PREFS_NAME = "TkKitchenPrefsData";

	private SensorEventReceiver mSimulationView;

	private OSCPortOut sender;

	private JmDNS jmdns;
	private ServiceListener listener;

	private EditText txtStatus;
	private EditText edtUserName;
	private boolean showData = false;

	private String userName;
	private MulticastLock multicastLock;

	private NTPTimeReceiver timeReceiver;
	private ServiceInfo kitchenServer;

	private int receiverTimeDelta;

	private EditText edtTimeOffset;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		startMulticast();

		setContentView(R.layout.main);

		userName = "user" + (int) Math.ceil(Math.random() * 100);
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		userName = settings.getString("userName", userName);
		edtUserName = (EditText) findViewById(R.id.edtUserName);
		edtTimeOffset = (EditText) findViewById(R.id.edtTimeOffset);
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

		TextWatcher offsetWatcher = new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			public void afterTextChanged(Editable arg0) {
				receiverTimeDelta = Integer.valueOf(edtTimeOffset.getText().toString());
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		};
		edtTimeOffset.addTextChangedListener(offsetWatcher);
		
		txtStatus = (EditText) findViewById(R.id.txtStatus);
		setStatus("Starting. Not connected.");

		// Get an instance of the SensorManager
		mSimulationView = new SensorEventReceiver(this);

		// Get an IP server address using JmDNS.
		// Currently only one server is supported.
		try {
			jmdns = JmDNS.create();
			setStatus("Started. Searching...");
			jmdns.addServiceListener(REMOTE_TYPE, listener = new ServiceListener() {

				public void serviceResolved(ServiceEvent ev) {
					connect(ev.getInfo());
				}

				public void serviceRemoved(ServiceEvent ev) {
					disconnect();
				}

				public void serviceAdded(ServiceEvent event) {
					jmdns.requestServiceInfo(event.getType(), event.getName(), true);
				}

				private void disconnect() {
					kitchenServer = null;
					stopTimeReceiver();
					sender = null;
					setStatus("Not connected! Searching");
				}

				private void connect(final ServiceInfo info) {
					kitchenServer = info;
					startTimeReceiver();
					try {
						sender = new OSCPortOut(info.getInetAddresses()[0], info.getPort());
						setStatus("Connected to: " + info.getInetAddresses()[0] + ":" + info.getPort());
					} catch (final SocketException e) {
						setStatus("Connection problem: " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		} catch (Exception ex) {
			setStatus("Error: " + ex.getMessage());
		}
	}
	
	public void transmitData(SensorData data) {
		// If an IP address for a data receiver is set, transmit the sensor
		// data.
		if (sender != null) {
			try {
				data.setTimeDelta(receiverTimeDelta);
				OSCMessage msg = new OSCMessage("/android/" + userName, data.toOSCArray());
				sender.send(msg);
			} catch (Exception e) {
				setStatus("Send Error: " + e.getMessage());
				e.printStackTrace();
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

	@Override
	protected void onStart() {
		super.onStart();
		startTimeReceiver();
	}
	
	public void setStatus(final String status) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {				
				txtStatus.setText(status);
			}
		});
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

		stopTimeReceiver();
		
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("userName", userName);

		editor.commit();
	}



	private void startMulticast() { // to be called by onCreate
		android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
		multicastLock = wifi.createMulticastLock("HeeereDnssdLock");
		multicastLock.setReferenceCounted(true);
		multicastLock.acquire();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (multicastLock != null) {
			multicastLock.release();
		}
	}

	private void startTimeReceiver() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				stopTimeReceiver();
				if (kitchenServer != null) {
					final EditText edtOffset = (EditText) findViewById(R.id.edtTimeOffset);
					final ImageView imageView = (ImageView) findViewById(R.id.ntpStatusView);
					timeReceiver = new NTPTimeReceiver(edtOffset, imageView);
					timeReceiver.execute(kitchenServer.getInetAddresses()[0]);
				}
			}
		});
		
	}
	
	private void stopTimeReceiver() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (timeReceiver != null) {
					timeReceiver.cancel(true);
					timeReceiver = null;
				}
			}
		});
	}
}