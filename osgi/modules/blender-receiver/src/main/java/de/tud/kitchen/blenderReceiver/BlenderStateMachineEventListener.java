package de.tud.kitchen.blenderReceiver;

public interface BlenderStateMachineEventListener {

	public void stateChanged(BlenderState newState, boolean isActive);
	
}
