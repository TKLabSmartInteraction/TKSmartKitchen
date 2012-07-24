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

package de.tud.kitchen.analyser.basics;

import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public abstract class Smoother<PRECISION> {

	protected AccelerometerEvent<PRECISION> currentSmoothedAccelerometerEvent;
	protected final String sender;
	protected final float smoothingAlpha;
	protected final float oneMinusSmoothingAlpha;

	public Smoother(String sender, float smoothingAlpha) {
		if (!(smoothingAlpha >= 0.0 && smoothingAlpha <= 1.0))
			throw new IllegalArgumentException("Smoothing alpha must be between 0 and 1");
		this.sender = sender + "/smooth";
		this.smoothingAlpha = smoothingAlpha;
		this.oneMinusSmoothingAlpha = 1 - smoothingAlpha;
	}

	public AccelerometerEvent<PRECISION> push(AccelerometerEvent<PRECISION> evt) {
		calculateSmoothedAccelerometerEvent(evt);
		return currentSmoothedAccelerometerEvent;
	}

	public abstract void calculateSmoothedAccelerometerEvent(AccelerometerEvent<PRECISION> evt);

	public static class DoubleSmoother extends Smoother<Double> {
		public DoubleSmoother(String sender, float smoothingAlpha) {
			super(sender, smoothingAlpha);
			this.currentSmoothedAccelerometerEvent = new AccelerometerEvent<Double>(sender, 0, 0.0, 0.0, 0.0);
		}

		public void calculateSmoothedAccelerometerEvent(de.tud.kitchen.api.event.acc.AccelerometerEvent<Double> evt) {
			currentSmoothedAccelerometerEvent = new AccelerometerEvent<Double>(sender, evt.timestamp,
					currentSmoothedAccelerometerEvent.x * oneMinusSmoothingAlpha + evt.x * smoothingAlpha,
					currentSmoothedAccelerometerEvent.y * oneMinusSmoothingAlpha + evt.y * smoothingAlpha,
					currentSmoothedAccelerometerEvent.z * oneMinusSmoothingAlpha + evt.z * smoothingAlpha);
		};
	}
	
	public static class FloatSmoother extends Smoother<Float> {
		public FloatSmoother(String sender, float smoothingAlpha) {
			super(sender, smoothingAlpha);
			this.currentSmoothedAccelerometerEvent = new AccelerometerEvent<Float>(sender, 0, 0.0f, 0.0f, 0.0f);
		}

		public void calculateSmoothedAccelerometerEvent(de.tud.kitchen.api.event.acc.AccelerometerEvent<Float> evt) {
			currentSmoothedAccelerometerEvent = new AccelerometerEvent<Float>(sender, evt.timestamp,
					currentSmoothedAccelerometerEvent.x * oneMinusSmoothingAlpha + evt.x * smoothingAlpha,
					currentSmoothedAccelerometerEvent.y * oneMinusSmoothingAlpha + evt.y * smoothingAlpha,
					currentSmoothedAccelerometerEvent.z * oneMinusSmoothingAlpha + evt.z * smoothingAlpha);
		};
	}

}
