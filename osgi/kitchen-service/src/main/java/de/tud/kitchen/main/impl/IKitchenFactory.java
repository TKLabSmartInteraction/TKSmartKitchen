package de.tud.kitchen.main.impl;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModule;

interface IKitchenFactory {

	Kitchen createKitchen(KitchenModule module);
	
}
