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

package de.tud.kitchen.dsensingni;


import java.net.SocketException;

import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCPortIn;

import de.tud.kitchen.api.Kitchen;
import de.tud.kitchen.api.module.KitchenModuleActivator;

public class Activator extends KitchenModuleActivator {
	
	private OSCPortIn dsensingni;
	
	@Override
	public void start(BundleContext context) throws Exception {
//		System.out.println("DsensingNI Bundle start called");
		super.start(context);
	}

	@Override
	public void start(Kitchen kitchen) {
		try {
			dsensingni = new OSCPortIn(3333);
			dsensingni.addListener("/tuio/3Dcur", new DSensingNICursorEventConverter(kitchen));
			dsensingni.addListener("/tuio/3Dblb", new DSensingNIObjectEventConverter(kitchen));
			dsensingni.startListening();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() {
		dsensingni.stopListening();
		dsensingni.close();
		dsensingni = null;
	}
}