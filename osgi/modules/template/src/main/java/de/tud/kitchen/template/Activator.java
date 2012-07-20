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

package de.tud.kitchen.template;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.illposed.osc.OSCPortIn;

public class Activator implements BundleActivator {
	OSCPortIn port;
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting Bundle");
		port = new OSCPortIn(3333);
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Stopping Bundle");
		port.close();
	}
}