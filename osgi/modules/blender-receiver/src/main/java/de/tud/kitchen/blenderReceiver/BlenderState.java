package de.tud.kitchen.blenderReceiver;

import java.util.Vector;

public class BlenderState {

	private String humanReadableName;
	private int ID;
	private Vector<BlenderTransition> transitions = new Vector<BlenderTransition>();
	private Vector<BlenderStateMachineEventListener> eventListeners = new Vector<BlenderStateMachineEventListener>();
	
	public BlenderState(String humanReadableName, int ID){
		this.humanReadableName = humanReadableName;
		this.ID = ID;
	}
	
	public BlenderState inputAction(BlenderAction action){
		BlenderState targetState;
		for(BlenderTransition transition : transitions){
			 targetState = transition.isFired(action);
			 if(targetState != null)
				 return targetState;
		}
		return null;
	}
	
	public void addTransition(BlenderTransition transition){
		transitions.add(transition);
	}
	
	public void addEventListener(BlenderStateMachineEventListener listener){
		eventListeners.add(listener);
	}
	
	public void notifyStateChange(boolean isActive){
		for(BlenderStateMachineEventListener listener : eventListeners){
			listener.stateChanged(this, isActive);
		}
	}

	public String getHumanReadableName() {
		return humanReadableName;
	}

	public int getID() {
		return ID;
	}

	
}
