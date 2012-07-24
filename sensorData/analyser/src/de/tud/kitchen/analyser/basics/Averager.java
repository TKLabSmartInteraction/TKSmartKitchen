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

import java.util.LinkedList;

import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public abstract class Averager<PRECISION> {

	protected LinkedList<AccelerometerEvent<PRECISION>> seenEvents;
	protected AccelerometerEvent<PRECISION> currentAverageAccelerometerEvent;
	protected String sender;
	protected int windowSize;

	public Averager(String sender, int windowSize) {
		this.windowSize = windowSize;
		this.sender = sender + "/avg";
		seenEvents = new LinkedList<AccelerometerEvent<PRECISION>>();
		currentAverageAccelerometerEvent = createEmptyAccelerometerEvent();
	}

	public AccelerometerEvent<PRECISION> push(AccelerometerEvent<PRECISION> evt) {
		seenEvents.push(evt);
		addToInternalEvent(evt);
		if (seenEvents.size() > windowSize) {
			AccelerometerEvent<PRECISION> oldEvt = seenEvents.pollLast();
			subFromInternalEvent(oldEvt);
		}
		return averageWith(seenEvents.size());
	}

	protected abstract AccelerometerEvent<PRECISION> createEmptyAccelerometerEvent();

	protected abstract void addToInternalEvent(AccelerometerEvent<PRECISION> evt);

	protected abstract void subFromInternalEvent(AccelerometerEvent<PRECISION> evt);

	protected abstract AccelerometerEvent<PRECISION> averageWith(int windowSize);

	public static class DoubleAverager extends Averager<Double> {

		public DoubleAverager(String sender, int windowSize) {
			super(sender, windowSize);
		}

		@Override
		protected void addToInternalEvent(AccelerometerEvent<Double> evt) {
			this.currentAverageAccelerometerEvent = new AccelerometerEvent<Double>(sender, evt.timestamp,
					currentAverageAccelerometerEvent.x + evt.x, currentAverageAccelerometerEvent.y + evt.y,
					currentAverageAccelerometerEvent.z + evt.z);
		}

		@Override
		protected AccelerometerEvent<Double> averageWith(int windowSize) {
			return new AccelerometerEvent<Double>(sender, currentAverageAccelerometerEvent.timestamp,
					currentAverageAccelerometerEvent.x/windowSize, currentAverageAccelerometerEvent.y/windowSize,
					currentAverageAccelerometerEvent.z/windowSize);
		}

		@Override
		public AccelerometerEvent<Double> createEmptyAccelerometerEvent() {
			return new AccelerometerEvent<Double>(sender, 0, 0.0, 0.0, 0.0);
		}

		@Override
		protected void subFromInternalEvent(AccelerometerEvent<Double> evt) {
			this.currentAverageAccelerometerEvent = new AccelerometerEvent<Double>(sender, evt.timestamp,
					currentAverageAccelerometerEvent.x - evt.x, currentAverageAccelerometerEvent.y - evt.y,
					currentAverageAccelerometerEvent.z - evt.z);
		}
	}
	
	public static class FloatAverager extends Averager<Float> {

		public FloatAverager(String sender, int windowSize) {
			super(sender, windowSize);
		}

		@Override
		protected void addToInternalEvent(AccelerometerEvent<Float> evt) {
			this.currentAverageAccelerometerEvent = new AccelerometerEvent<Float>(sender, evt.timestamp,
					currentAverageAccelerometerEvent.x + evt.x, currentAverageAccelerometerEvent.y + evt.y,
					currentAverageAccelerometerEvent.z + evt.z);
		}

		@Override
		protected AccelerometerEvent<Float> averageWith(int windowSize) {
			return new AccelerometerEvent<Float>(sender, currentAverageAccelerometerEvent.timestamp,
					currentAverageAccelerometerEvent.x/windowSize, currentAverageAccelerometerEvent.y/windowSize,
					currentAverageAccelerometerEvent.z/windowSize);
		}

		@Override
		public AccelerometerEvent<Float> createEmptyAccelerometerEvent() {
			return new AccelerometerEvent<Float>(sender, 0, 0.0f, 0.0f, 0.0f);
		}

		@Override
		protected void subFromInternalEvent(AccelerometerEvent<Float> evt) {
			this.currentAverageAccelerometerEvent = new AccelerometerEvent<Float>(sender, evt.timestamp,
					currentAverageAccelerometerEvent.x - evt.x, currentAverageAccelerometerEvent.y - evt.y,
					currentAverageAccelerometerEvent.z - evt.z);
		}
	}
}
