package de.tud.kitchen.apps.eventinspector.rtgraph;

import info.monitorenter.gui.chart.Chart2D;
import info.monitorenter.gui.chart.rangepolicies.RangePolicyHighestValues;
import info.monitorenter.gui.chart.traces.Trace2DLtd;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.acc.AccelerometerEvent;
import de.tud.kitchen.apps.eventinspector.DynamicEventFilter;
import de.tud.kitchen.apps.eventinspector.DynamicEventFilter.DynamicEventFilterDelegate;

public class GraphWindow extends JFrame {

	private JPanel contentPane;
	private Chart2D sumChart2d;
	private Chart2D chart2d;
	private List<Trace2DLtd> traceSums = new ArrayList<Trace2DLtd>(); 

	/**
	 * Create the frame.
	 */
	public GraphWindow() {
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		sumChart2d = new Chart2D();
		sumChart2d.getAxisX().setRangePolicy(new RangePolicyHighestValues(5000));
		contentPane.add(sumChart2d);
		
	}

	public Trace2DLtd addTrace(String name) {
		final Trace2DLtd trace = new Trace2DLtd(200,name);
		chart2d.addTrace(trace);
		return trace;
	};
	
	private void appendChart() {
		chart2d = new Chart2D();
		chart2d.getAxisX().setRangePolicy(new RangePolicyHighestValues(5000));
		try {
			SwingUtilities.invokeAndWait(new Runnable() {	
				@Override
				public void run() {
					contentPane.add(chart2d);
					contentPane.invalidate();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public DynamicEventFilter getEventFilter() {
		return eventFilter;
	}

	private DynamicEventFilter eventFilter = new DynamicEventFilter(new DynamicEventFilterDelegate() {
		HashMap<String, Trace2DLtd[]> traces = new HashMap<String, Trace2DLtd[]>();
		@Override
		public void handleEvent(final Event event) {
			final AccelerometerEvent<?> accEvent = (AccelerometerEvent<?>) event;
			Trace2DLtd[] array;
			if (!traces.containsKey(event.sender)) {
				appendChart();
				final Trace2DLtd trace = new Trace2DLtd(200,event.sender + " abs");
				sumChart2d.addTrace(trace);
				traces.put(event.sender, new Trace2DLtd[] {addTrace(event.sender + "/x"),addTrace(event.sender + "/y"),addTrace(event.sender + "/z"), trace});
				array = traces.get(event.sender);
				array[0].setColor(Color.BLUE);
				array[1].setColor(Color.RED);
				array[2].setColor(Color.GREEN);
			} else {
				array = traces.get(event.sender);
			}
			final Trace2DLtd[] traceArray = array;
			if (accEvent.x instanceof Float) {
				traceArray[0].addPoint(accEvent.timestamp, (Float) accEvent.x);
				traceArray[1].addPoint(accEvent.timestamp, (Float) accEvent.y);
				traceArray[2].addPoint(accEvent.timestamp, (Float) accEvent.z);
				traceArray[3].addPoint(accEvent.timestamp, new Point3f((Float) accEvent.x, (Float) accEvent.y, (Float) accEvent.z).distance(new Point3f()));
			} else if (accEvent.x instanceof Double) {
				traceArray[0].addPoint(accEvent.timestamp, (Double) accEvent.x);
				traceArray[1].addPoint(accEvent.timestamp, (Double) accEvent.y);
				traceArray[2].addPoint(accEvent.timestamp, (Double) accEvent.z);
				traceArray[3].addPoint(accEvent.timestamp, new Point3d((Double) accEvent.x, (Double) accEvent.y, (Double) accEvent.z).distance(new Point3d()));
			}
		}
	});
}
