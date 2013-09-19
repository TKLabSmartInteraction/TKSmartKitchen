package de.tud.kitchen.scale;

import de.tud.kitchen.api.event.Event;

public class ScaleEvent extends Event {
	public int scaleID;
	public int weight;

	public ScaleEvent(int scaleID, int weight) {
		super("Scale " + scaleID);

		this.scaleID = scaleID;
		this.weight = weight;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", super.toString(), "Weight changed to: " + weight + "g");
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return ", weightInGrams";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format(", %s", weight);
	}

}
