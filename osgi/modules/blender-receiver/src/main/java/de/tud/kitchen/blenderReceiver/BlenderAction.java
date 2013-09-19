package de.tud.kitchen.blenderReceiver;

public class BlenderAction {

	private int buttonID;
	private boolean pushed;
	
	public BlenderAction(int buttonID, boolean pushed){
		this.buttonID = buttonID;
		this.pushed = pushed;
	}
	
	public int getButtonID() {
		return buttonID;
	}

	public boolean isPushed() {
		return pushed;
	}
	
	public boolean equals(Object other){
		if(this == other) // check for identity
			return true;
		if(other != null && other instanceof BlenderAction){ // if other object is not null and same class
			return (this.buttonID == ((BlenderAction)other).buttonID && this.pushed == ((BlenderAction)other).pushed); // check actual data fields
		} 
		else {
			return false;
		}
		
	}
	
}
