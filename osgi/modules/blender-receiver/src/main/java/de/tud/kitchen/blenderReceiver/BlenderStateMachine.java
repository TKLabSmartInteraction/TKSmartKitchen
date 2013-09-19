package de.tud.kitchen.blenderReceiver;

public class BlenderStateMachine {
	
	private BlenderState currentState;
	
	public BlenderStateMachine(BlenderState defaultState){
		currentState = defaultState;
	}
	
	public void inputAction(BlenderAction action){
		BlenderState newState = currentState.inputAction(action);
		if(newState != null){
			currentState.notifyStateChange(false);
			newState.notifyStateChange(true);
			currentState = newState;
		}
	}
	
}
