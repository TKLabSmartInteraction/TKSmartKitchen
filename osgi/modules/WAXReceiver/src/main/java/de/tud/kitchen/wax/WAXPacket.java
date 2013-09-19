package de.tud.kitchen.wax;

public class WAXPacket {
	
	private int deviceID;
	private boolean batteryLow;
	private byte sensorRange;
	private byte dataFormat;
	private int samplingFrequency;
	private int sequenceID;
	private int containedSamples;
	private int sendbufferSize;
	private WAXSample[] samples;
	
	public int getDeviceID() {
		return deviceID;
	}

	public void setDeviceID(int deviceID) {
		this.deviceID = deviceID;
	}

	public boolean isBatteryLow() {
		return batteryLow;
	}

	public void setBatteryLow(boolean batteryLow) {
		this.batteryLow = batteryLow;
	}

	public byte getSensorRange() {
		return sensorRange;
	}

	public void setSensorRange(byte sensorRange) {
		this.sensorRange = sensorRange;
	}

	public byte getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(byte dataFormat) {
		this.dataFormat = dataFormat;
	}

	public int getSamplingFrequency() {
		return samplingFrequency;
	}

	public void setSamplingFrequency(int samplingFrequency) {
		this.samplingFrequency = samplingFrequency;
	}

	public int getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}

	public int getContainedSamples() {
		return containedSamples;
	}

	public void setContainedSamples(int containedSamples) {
		this.containedSamples = containedSamples;
	}

	public WAXSample getSample(int i) {
		return samples[i];
	}

	public void setSamples(WAXSample[] samples) {
		this.samples = samples;
	}
	
	public int getSendbufferSize() {
		return sendbufferSize;
	}

	public void setSendbufferSize(int sendbufferSize) {
		this.sendbufferSize = sendbufferSize;
	}
	
}
