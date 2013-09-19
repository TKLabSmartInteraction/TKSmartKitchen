package de.tud.kitchen.scale;

import java.util.Arrays;

public class MedianRingbuffer extends Ringbuffer implements Buffer {
	
	public MedianRingbuffer(int size) {
		super(size);
	}
	
	public int getMedianValue(){
		int[] bufferCopy = buffer.clone();
		Arrays.sort(bufferCopy);
		if(size % 2 == 0){
			return ((bufferCopy[(size/2)-1] + bufferCopy[(size/2)]) / 2);
		} else {
			return bufferCopy[(size-1)/2];
		}
	}

}
