package de.tud.kitchen.scale;

import java.lang.Math;

public class ScaleEdgeDetector implements EdgeDetector {

	public ScaleEdgeDetector(int windowSize, int threshold, int resolution, int hysteresis){
		this.threshold = threshold;
		this.resolution = resolution;
		this.hysteresis = hysteresis;
		this.buffer1 = new MedianRingbuffer(windowSize);
		this.buffer2 = new MedianRingbuffer(windowSize);
		this.buffer3 = new MedianRingbuffer(windowSize);
		this.buffer4 = new MedianRingbuffer(windowSize);
		buffer1.initFields(0);
		buffer2.initFields(0);
	}
	
	private int threshold;
	private int resolution;
	private int hysteresis;
	private int lastEdge = 0;
	private MedianRingbuffer buffer1;
	private MedianRingbuffer buffer2;
	private MedianRingbuffer buffer3;
	private MedianRingbuffer buffer4;
	
	@Override
	public int getEdge(int nextValue) {
		buffer4.addValue(buffer3.addValue(buffer2.addValue(buffer1.addValue(nextValue)))); // add value to concatinated buffers
		int median1 = buffer1.getMedianValue();
		int median2 = buffer2.getMedianValue();
		int median3 = buffer3.getMedianValue();
		int median4 = buffer4.getMedianValue();
		if((Math.abs(median3 - median4) > threshold) && (Math.abs(median1 - median2) < resolution) && (Math.abs(median1 - lastEdge) > hysteresis)){
			lastEdge = median1; // refresh last detected edge
			return median1;
		} else {
			return -1;
		}
	}

}
