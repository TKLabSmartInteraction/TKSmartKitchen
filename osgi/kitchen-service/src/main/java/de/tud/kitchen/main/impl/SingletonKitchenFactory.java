package de.tud.kitchen.main.impl;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModule;

public class SingletonKitchenFactory implements IKitchenFactory {

	Kitchen kitchen = new KitchenImpl();
	
	@Override
	public Kitchen createKitchen(KitchenModule module) {
		return kitchen;
	}
}
