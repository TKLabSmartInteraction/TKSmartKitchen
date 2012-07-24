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

package de.tud.kitchen.android;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *
 */
public class NTPTimeReceiver extends AsyncTask<InetAddress, Integer, Void>{

	private ImageView statusIcon;
	private TextView offsetText;

	public NTPTimeReceiver(TextView offsetText, ImageView statusIcon ) {
		this.offsetText = offsetText;
		this.statusIcon = statusIcon;
	}
	
	
	@Override
	protected Void doInBackground(InetAddress... params) {
		NTPUDPClient client = new NTPUDPClient();
		client.setDefaultTimeout(10000);
		try {
			client.open();
		} catch (final SocketException se) {
			se.printStackTrace();
			return null;
		} 
		while(!this.isCancelled()) {
			try {
				TimeInfo info = client.getTime(params[0]);
				info.computeDetails();
				Long offsetValue = info.getOffset();
				int receiverTimeDelta = (offsetValue == null) ? 0 : offsetValue.intValue();
				publishProgress(receiverTimeDelta);
			} catch (final IOException ioe) {
				ioe.printStackTrace();
				continue;
			}
			
			try {
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				continue;
			}
		}
		client.close();
		return null;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		statusIcon.setImageResource(android.R.drawable.presence_away);
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		offsetText.setText(String.valueOf(0));
		statusIcon.setImageResource(android.R.drawable.presence_offline);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		offsetText.setText(String.valueOf(values[0]));
		statusIcon.setImageResource(android.R.drawable.presence_online);
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		offsetText.setText(String.valueOf(0));
		statusIcon.setImageResource(android.R.drawable.presence_offline);
	}
	
}
