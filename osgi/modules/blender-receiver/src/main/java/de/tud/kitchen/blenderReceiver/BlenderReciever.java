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
public class BlenderReciever extends Service implements IBlenderEvent {
	EventPublisher<BlenderEvent> eventpublisher;
	
	public static final String DEFAULT_ZONE = "lan";
    public static final String DEFAULT_CHANNEL = "kaffeekueche.blender.event";
    
    
    
	public BlenderReciever(EventPublisher<BlenderEvent> publisher) {
		this.eventpublisher = publisher;
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
		eventpublisher.publish(new BlenderEvent(""+but, true));
	}
	
	@Override
	public void buttonReleased(int but) {
		eventpublisher.publish(new BlenderEvent(""+but, true));
	}

}