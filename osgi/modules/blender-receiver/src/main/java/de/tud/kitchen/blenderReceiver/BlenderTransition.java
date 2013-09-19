package de.tud.kitchen.blenderReceiver;

public class BlenderTransition {

	private BlenderAction action;
	private BlenderState targetState;
	
	public BlenderTransition(BlenderAction action, BlenderState targetState){
		this.action = action;
		this.targetState = targetState;
	}
	
	public BlenderState isFired(BlenderAction action){
		//System.out.println("Checking action hash:" + action.hashCode());
		//System.out.println("Against transitions action hash:" + this.action.hashCode());
		if(this.action.equals(action)){
			return this.targetState;
		}
		else {
			return null;
		}
	}
	
}
