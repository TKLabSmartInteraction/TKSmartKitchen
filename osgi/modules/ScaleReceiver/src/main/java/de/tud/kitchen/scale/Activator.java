package de.tud.kitchen.scale;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.event.rfid.RfidEvent;
import de.tud.kitchen.api.module.KitchenModuleActivator;

import com.phidgets.BridgePhidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.BridgeDataEvent;
import com.phidgets.event.BridgeDataListener;

import java.lang.Math;

public class Activator extends KitchenModuleActivator implements AttachListener, BridgeDataListener {
	
	private final int SERIALNO = 141152; // serial number of opened RFID-phidget
	
	private EventPublisher<ScaleEvent> publisher;
	private BridgePhidget wheatstoneBridge;
	private ScaleEdgeDetector detector;
	// config
	private boolean csvOutEnable = false; //TODO: disable
	private boolean scaleCutoffFilterDisable = false; //TODO: disable cutoff

	@Override
	public void start(Kitchen kitchen) {
		publisher = kitchen.getEventPublisher(ScaleEvent.class);
		detector = new ScaleEdgeDetector(10, 50, 5, 20);

		try {
			wheatstoneBridge = new BridgePhidget();
			wheatstoneBridge.addAttachListener(this);
			wheatstoneBridge.addBridgeDataListener(this);
			wheatstoneBridge.open(SERIALNO); // identify wheatstone-phidget by serial number
		} catch (PhidgetException e) {
			e.printStackTrace(); // TODO: rethrow
		}
	}

	@Override
	public void stop() {
		try {
			wheatstoneBridge.close();
		} catch (PhidgetException e) {
			e.printStackTrace(); // TODO: rethrow
		}
	}
	

	@Override
	public void attached(AttachEvent ae) {
		try {
			((BridgePhidget)ae.getSource()).setDataRate(100);
			((BridgePhidget)ae.getSource()).setGain(0, BridgePhidget.PHIDGET_BRIDGE_GAIN_128);
			((BridgePhidget)ae.getSource()).setEnabled(0, true);
		} catch (PhidgetException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void bridgeData(BridgeDataEvent arg0) {
		int weight = (int)Math.round(6380.7602 * arg0.getValue() + 3310.2655);
		int edge = -1; // default to non-edge
		if(scaleCutoffFilterDisable || weight < 3000)
			edge = detector.getEdge(weight);
		//System.out.println("ScaleReceiver: Weight: " + weight + "g");
		if(edge != -1){
			publisher.publish(new ScaleEvent(SERIALNO, edge));
		}
		// CSV-output f. filter-comapre
		if(csvOutEnable)
			System.out.println(weight + "," + edge);
	}
}