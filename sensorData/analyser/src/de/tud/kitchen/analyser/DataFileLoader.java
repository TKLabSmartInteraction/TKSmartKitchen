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

package de.tud.kitchen.analyser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public class DataFileLoader {
	private static String base_path = "../";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedHashSet<AccelerometerEvent<Float>> data = loadDataFile("07_02/splitted/103");

		for (AccelerometerEvent<Float> entry : data) {
			System.out.println(entry.timestamp + ":  " + entry.x);
		}
	}

	/**
	 * Reads the given file in the sensorData directory into a LinkedHashSet.
	 * 
	 * @param filename
	 *            The file containing the sensor data.
	 * @return The sensor data as AccelerometerEvent list.
	 */
	public static LinkedHashSet<AccelerometerEvent<Float>> loadDataFile(String filename) {
		LinkedHashSet<AccelerometerEvent<Float>> acc_data = new LinkedHashSet<AccelerometerEvent<Float>>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(base_path + filename));
			String inLine = null;

			while ((inLine = reader.readLine()) != null) {
				String[] row = inLine.split(" ");

				AccelerometerEvent<Float> data_entry = new AccelerometerEvent<Float>(filename, Long.valueOf(row[0]).longValue(),
						Float.valueOf(row[1]), Float.valueOf(row[2]), Float.valueOf(row[3]));
				acc_data.add(data_entry);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return acc_data;
	}
}
