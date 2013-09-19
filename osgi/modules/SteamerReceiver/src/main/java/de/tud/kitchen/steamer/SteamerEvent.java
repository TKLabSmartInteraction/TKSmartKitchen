package de.tud.kitchen.steamer;

import de.tud.kitchen.api.event.Event;

public class SteamerEvent extends Event {
	public int steamerID;
	public boolean steamingMode;

	public SteamerEvent(int steamerID, boolean steamingMode) {
		super("Steamer " + steamerID);

		this.steamerID = steamerID;
		this.steamingMode = steamingMode;
	}

	@Override
	public String toString() {
		return String.format("%s, %s", super.toString(), (steamingMode ? "Steaming started" : "Steaming stopped"));
	}

	@Override
	protected String getAdditionalCsvHeader() {
		return ", steaming_mode";
	}

	@Override
	protected String getAdditionalCsvValues() {
		return String.format(", %s, %s", (steamingMode ? "steaming-start" : "steaming-stop"));
	}

}
