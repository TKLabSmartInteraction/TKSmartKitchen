package de.tud.kitchen.api.module;

import de.tud.kitchen.api.Kitchen;

public interface KitchenModule {

	void start(Kitchen kitchen);
	void stop();
}
