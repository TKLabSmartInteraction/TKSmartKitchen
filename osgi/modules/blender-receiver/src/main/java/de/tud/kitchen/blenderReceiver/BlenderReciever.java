/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

package de.tud.kitchen.blenderReceiver;

//@mcImport
import org.mundo.blender.IBlenderEvent;
import org.mundo.rt.Service; 
import org.mundo.rt.Signal;

import de.tud.kitchen.api.event.EventPublisher;

/**
 * The {@link BlenderReciever} will subscribe as an {@link IBlenderEvent} to recieve coffee machine events.
 *
 *
 * @version    1.0.0, 15.08.2011
 * @author     Marcus St&auml;nder, Aristotelis Hadjakos    
 */
public class BlenderReciever extends Service implements IBlenderEvent, BlenderStateMachineEventListener {
	EventPublisher<BlenderEvent> eventpublisher;
	
	BlenderStateMachine blenderFSM;
	BlenderState blenderOff, blenderOn, blenderActive;
	//BlenderAction a0, a1, a2;
	
	public static final String DEFAULT_ZONE = "lan";
    public static final String DEFAULT_CHANNEL = "kaffeekueche.blender.event";
    
	public BlenderReciever(EventPublisher<BlenderEvent> publisher) {
		this.eventpublisher = publisher;
		
		/* configure blender states */
		blenderOff = new BlenderState("blenderOff", 0);
		blenderOn = new BlenderState("blenderOn", 1);
		blenderActive = new BlenderState("blenderActive", 2);
		blenderOff.addTransition(new BlenderTransition(new BlenderAction(DIAL_ON, true), blenderOn));
		blenderOn.addTransition(new BlenderTransition(new BlenderAction(16, true), blenderActive));
		blenderActive.addTransition(new BlenderTransition(new BlenderAction(16, false), blenderOn));
		
		/* configure blender state machine */
		blenderFSM = new BlenderStateMachine(blenderOff); // set off as default state
		
		/* add event listener for specific states */
		blenderActive.addEventListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override public void init() {
		super.init();
        setServiceZone("lan");
        setServiceInstanceName("BlenderReciever");
		Signal.connect(getSession().subscribe(DEFAULT_ZONE, DEFAULT_CHANNEL), this);
	}

	@Override
	public void buttonPushed(int but) {
		if(but == 1 || but == 2 || but == 4){ // special buttons pushed
			System.out.println("Passing action: Button " + but + "pressed: true");
			eventpublisher.publish(new BlenderEvent("Blender1",""+but, true));
		}
		else {
			//System.out.println("Passing action to blenderFSM: Button " + but + "pressed: true");
			blenderFSM.inputAction(new BlenderAction(but, true));
		}
	}
	
	@Override
	public void buttonReleased(int but) {
		if(but == 1 || but == 2 || but == 4){ // special buttons pushed
			System.out.println("Passing action: Button " + but + "pressed: false");
			eventpublisher.publish(new BlenderEvent("Blender1",""+but, false));
		}
		else {
			//System.out.println("Passing action to blenderFSM: Button " + but + "pressed: false");
			blenderFSM.inputAction(new BlenderAction(but, false));
		}
	}

	@Override
	public void stateChanged(BlenderState newState, boolean isActive) {
		//System.out.println("New blender state: " + newState.getHumanReadableName() + " is active: " + isActive);
		eventpublisher.publish(new BlenderEvent("Blender1","0", isActive));
	}

}
