package de.tud.kitchen.steamer;

public class SteamerMapping {
	public static final int CONTROL_BUTTON_MINUS = 4;
	public static final int CONTROL_BUTTON_PLUS = 1;
	public static final int CONTROL_BUTTON_ON = 7;
	public static final int CONTROL_BUTTON_FISH = 6;
	public static final int CONTROL_BUTTON_RICE = 2;
	public static final int CONTROL_BUTTON_CARROT = 5;
	public static final int CONTROL_BUTTON_POTATO = -1;
	public static final int CONTROL_BUTTON_CHICKEN = 3;
	public static final int CONTROL_BUTTON_KEEP_WARM = -1;
	//public static final int CONTROL_BUZZER_ENABLE = 0;	// changed hardware configuration -> buzzer enable function removed
	//public static final int SENSE_POWER = 1;				// not working at all				
	public static final int SENSE_STEAMING = 0;				// changed hardware configuration -> steaming reported on digital input 0
	public static final int SENSE_DESCALE = -2;
	public static final int SENSE_REFILL = -3;
	
	public static final int BUTTON_PRESSED_DURATION = 80;
}
