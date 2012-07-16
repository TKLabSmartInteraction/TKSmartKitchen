package de.tud.kitchen.main.impl;

import de.tud.kitchen.api.module.KitchenModule;

public class KitchenModuleManager {

	private final IKitchenFactory factory;
	
	public KitchenModuleManager(IKitchenFactory factory) {
		this.factory = factory;
	}

	public void add(KitchenModule module) {
		module.start(factory.getSandboxedKitchen(module));
	}
	
	public void remove(KitchenModule module) {
		factory.getSandboxedKitchen(module).stop();
		module.stop();
	}
	
}
