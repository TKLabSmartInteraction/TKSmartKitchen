package de.tud.kitchen.main.impl;

import de.tud.kitchen.api.module.KitchenModule;

interface IKitchenFactory {

	KitchenInternal getSingletonKitchen();

	KitchenInternal getSandboxedKitchen(KitchenModule module);
	
}
