package de.tud.kitchen.steamer;

import static de.tud.kitchen.steamer.SteamerMapping.BUTTON_PRESSED_DURATION;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_CARROT;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_CHICKEN;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_FISH;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_KEEP_WARM;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_MINUS;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_ON;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_PLUS;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_POTATO;
import static de.tud.kitchen.steamer.SteamerMapping.CONTROL_BUTTON_RICE;
import static de.tud.kitchen.steamer.SteamerMapping.SENSE_DESCALE;
import static de.tud.kitchen.steamer.SteamerMapping.SENSE_STEAMING;
import static de.tud.kitchen.steamer.SteamerMapping.SENSE_REFILL;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;

public class Steamer implements InputChangeListener{

	private EventCallback callback;
	private final InterfaceKitPhidget phidget;
	
	public Steamer(InterfaceKitPhidget phidget) {
		this.phidget = phidget;
	}
	
	public void setCallback(EventCallback callback) {
		this.callback = callback;
	}
	
	public void unsetCallback(){
		callback = null;
	}
	
	public void init() throws PhidgetException {
		int outputCount = phidget.getOutputCount();
		for ( int port = 0; port < outputCount; port++) {
			try {
				phidget.setOutputState(port, true);
			} catch (PhidgetException e) {e.printStackTrace();};
		}
		phidget.addInputChangeListener(this);
		System.out.println("Steamer: Called addInputChangeListener with object#: " + this.hashCode());
	}
	
	public void close() {
		phidget.removeInputChangeListener(this);
		System.out.println("Steamer: Called removeInputChangeListener with object#: " + this.hashCode());
	}
	
	private void pressButtonWithoutSound(int port) {
		try {
			//phidget.setOutputState(CONTROL_BUZZER_ENABLE, false);//disable buzzer
			togglePhidgetPort(port);
			Thread.sleep(BUTTON_PRESSED_DURATION);
			//phidget.setOutputState(CONTROL_BUZZER_ENABLE, true); //enable buzzer
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private void togglePhidgetPort(int port) {
		try {
			phidget.setOutputState(port, false);
			Thread.sleep(BUTTON_PRESSED_DURATION);
			phidget.setOutputState(port, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void togglePowerButton() {
			pressButtonWithoutSound(CONTROL_BUTTON_ON);
	}

	
	public void increaseTimer() {
		pressButtonWithoutSound(CONTROL_BUTTON_PLUS);
	}

	
	public void decreaseTimer() {
		pressButtonWithoutSound(CONTROL_BUTTON_MINUS);
	}
	
	
	public void selectFishTimer() {
		pressButtonWithoutSound(CONTROL_BUTTON_FISH);
	}

	
	public void selectFishMode() {
		pressButtonWithoutSound(CONTROL_BUTTON_FISH);
	}

	
	public void selectCarrotTimer() {
		pressButtonWithoutSound(CONTROL_BUTTON_CARROT);
	}

	
	public void selectPotatoTimer() {
		pressButtonWithoutSound(CONTROL_BUTTON_POTATO);
	}

	
	public void selectPoultryMode() {
		pressButtonWithoutSound(CONTROL_BUTTON_CHICKEN);
	}

	
	public void selectRiceMode() {
		pressButtonWithoutSound(CONTROL_BUTTON_RICE);
	}

	
	public void selectKeepWarmMode() {
		pressButtonWithoutSound(CONTROL_BUTTON_KEEP_WARM);
	}

	
	public boolean isLidClosed() {
		return true;
	}
	
	public void inputChanged(InputChangeEvent arg0) {
		switch (arg0.getIndex()) {
		case SENSE_STEAMING:
			if(arg0.getState() == true){
				callback.steamingOn();
			}
			else {
				callback.steamingOff();
			}
			break;
		case SENSE_DESCALE:
			callback.descale();
			break;
		case SENSE_REFILL:
			callback.refill();
			break;
		}
	}
	
	/*
		removed SteamerState-class
	*/
	
}
