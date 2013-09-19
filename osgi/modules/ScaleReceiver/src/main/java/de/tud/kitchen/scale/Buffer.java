package de.tud.kitchen.scale;

public interface Buffer {
	
	public int size();
	public void initFields(int value);
	public int addValue(int value);
	public int[] getBuffer();
	
}
