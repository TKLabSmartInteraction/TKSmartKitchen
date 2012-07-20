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

package de.tud.kitchen.analyser.view;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.IAxis.AxisTitle;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyHighestValues;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import de.tud.kitchen.api.event.acc.AccelerometerEvent;

public class GraphWindow extends JFrame {
	
	private Chart2D chart2d;
	float lastHueValue = 0.0f;
	
	
	public GraphWindow() {
		chart2d = new Chart2D();
		chart2d.getAxisX().setAxisTitle(new AxisTitle("time"));
		chart2d.getAxisY().setAxisTitle(new AxisTitle("value"));
		
		getContentPane().add(chart2d);
	}
	
	public void add(final List<AccelerometerEvent> sampleList) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Trace2DSimple trace = createNewTrace(sampleList.get(0));
				addSamplesToTrace(sampleList, trace);
			}

			private Trace2DSimple createNewTrace(AccelerometerEvent<?> accelerometerEvent) {
				final Trace2DSimple trace = new Trace2DSimple(accelerometerEvent.sender);
				trace.setColor(Color.getHSBColor(lastHueValue, 0.9f, 0.9f));
				chart2d.addTrace(trace);
				lastHueValue += 0.1f;
				if (lastHueValue > 1.0) {
					lastHueValue = (float) (Math.random()/10);
				}
				return trace;
			}
			
			private void addSamplesToTrace(List<AccelerometerEvent> samples, Trace2DSimple trace) {
				for (AccelerometerEvent<?> accEvent : samples) {					
					if (accEvent.x instanceof Float) {
						trace.addPoint(accEvent.timestamp, (Float) accEvent.x);
					} else if (accEvent.x instanceof Double) {
						trace.addPoint(accEvent.timestamp, (Double) accEvent.x);
					}
				}
			}
		});
	}
	
}