package de.tud.kitchen.dsensingni;

import java.util.Date;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;

import de.tud.kitchen.api.Kitchen;

public class DSensingNICursorEventConverter implements OSCListener {

	private final Kitchen kitchen;
	
	public DSensingNICursorEventConverter(Kitchen kitchen) {
		this.kitchen = kitchen;
	}
	
	@Override
	public void acceptMessage(Date arg0, OSCMessage arg1) {
		System.out.println(arg1);
	}
	
}
