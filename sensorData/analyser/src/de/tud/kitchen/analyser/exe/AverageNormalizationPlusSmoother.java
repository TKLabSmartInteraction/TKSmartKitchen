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

package de.tud.kitchen.analyser.exe;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3f;

import de.tud.kitchen.analyser.DataFileLoader;
import de.tud.kitchen.analyser.basics.Averager.FloatAverager;
import de.tud.kitchen.analyser.basics.Smoother.FloatSmoother;
import de.tud.kitchen.analyser.view.GraphManager;
import de.tud.kitchen.analyser.view.GraphWindow;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public class AverageNormalizationPlusSmoother {

	public static void main(String[] args) {
		GraphManager graphManager = new GraphManager();
		
		LinkedHashSet<AccelerometerEvent<Float>> data = DataFileLoader.loadDataFile("07_02/splitted/_android_brenner");
		
		FloatAverager averager = new FloatAverager(data.iterator().next().sender,15);
		LinkedList<AccelerometerEvent<Float>> flippedData = new LinkedList<AccelerometerEvent<Float>>();
		for (AccelerometerEvent<Float> accelerometerEvent : data) {
			final AccelerometerEvent<Float> newData = averager.push(accelerometerEvent);
			flippedData.add(new AccelerometerEvent<Float>(accelerometerEvent.sender+"/flip", accelerometerEvent.timestamp, Math.abs(accelerometerEvent.x - newData.x), Math.abs(accelerometerEvent.y - newData.y), Math.abs(accelerometerEvent.z - newData.z)));
		}
		
		LinkedList<AccelerometerEvent<Float>> distanceVectorData = new LinkedList<AccelerometerEvent<Float>>();
		for (AccelerometerEvent<Float> accEvent : flippedData) {
			distanceVectorData.add(new AccelerometerEvent<Float>(accEvent.sender+"/d", accEvent.timestamp, new Point3f(0,0,0).distance(new Point3f(accEvent.x, accEvent.y, accEvent.z)), 0.0f, 0.0f));
		}
		
		FloatSmoother smoother = new FloatSmoother(distanceVectorData.iterator().next().sender, 0.02f);
		List<AccelerometerEvent<Float>> smoothedData = new LinkedList<AccelerometerEvent<Float>>();
		for (AccelerometerEvent<Float> accelerometerEvent : distanceVectorData) {
			smoothedData.add(smoother.push(accelerometerEvent));
		}
		
		GraphWindow window = graphManager.createNewGraphWindow("smoothed");
//		window.add((List) new LinkedList(data));
		window.add((List) smoothedData);
	}
}
