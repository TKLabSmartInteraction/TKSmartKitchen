/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <niklas.lochschmidt@stud.tu-darmstadt.de>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

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
	
	public void stop() {
		factory.getSingletonKitchen().stop();
	}
	
}
