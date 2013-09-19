package de.tud.kitchen.scale;

public class Ringbuffer implements Buffer{
	
	public Ringbuffer(int size){
		this.buffer = new int[size];
		this.size = size;
	}

	protected int size = 0;
	protected int[] buffer = null;
	private int pointer = 0;
	
	public int size(){
		return size;
	}
	
	public void initFields(int value){
		for(int field : buffer){
			field = value;
		}
	}
	
	public int addValue(int value){
		int overwrittenValue = buffer[pointer];
		buffer[pointer++] = value;
		if(pointer >= size)
			pointer = 0;
		return overwrittenValue;
	}
	
	public int[] getBuffer(){
		return buffer;
	}
}
