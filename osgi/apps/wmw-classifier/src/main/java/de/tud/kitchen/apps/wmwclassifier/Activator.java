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

package de.tud.kitchen.apps.wmwclassifier;

import java.awt.EventQueue;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	
	@Override
	public void start(final Kitchen kitchen) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EventConsumer consumer = new WMWEventConsumer();
					kitchen.registerEventConsumer(consumer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void stop() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				// What to do if shut down?
			}
		});
	}
}