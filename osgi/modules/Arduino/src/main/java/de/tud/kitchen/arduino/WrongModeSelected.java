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

package de.tud.kitchen.arduino;


/**
 * just here to represent an Exception thrown when the board should do
 * command thats not supported in the selected mode.
 * @author Christian Klos
 *
 */
public class WrongModeSelected extends Exception{

	/**
	 * needs to be serialized
	 */
	private static final long serialVersionUID = -4243761085176726386L;
	
	

}
