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

package de.tud.kitchen.analyser.view;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

import com.sun.org.apache.bcel.internal.generic.NEW;


public class GraphManager {
	
	public GraphWindow createNewGraphWindow(String title) {
		final GraphWindow window = new GraphWindow();
		window.setTitle(title);
		window.setSize(500, 200);
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				window.setVisible(true);	
			}
		});
		return window;
	}
}
