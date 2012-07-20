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

package de.tud.kitchen.doorGui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.tud.kitchen.api.event.furniture.DoorEvent;

/**
 * A Gui that shows the DoorStatus of our doors in the Kitchen
 * @author Christian Klos
 *
 */
public class Gui {

	/**
	 * Buttons to represent A Door, the Doors are Labeled in this Order:
	 * 
	 * |-----------------------------|
	 * |         |    B    |    D    |
	 * |         +---------+---------|
	 * |         |         |         |
	 * |    A    |         |         |
	 * |         |    C    |    E    |
	 * |         |         |         |
	 * |         |         |         |
	 * |-----------------------------|
	 * 
	 */
	final JButton a = new JButton("A");
	final JButton b = new JButton("B");
	final JButton c = new JButton("C");
	final JButton d = new JButton("D");
	final JButton e = new JButton("E");

	/**
	 * Constructor places the Window and all the Buttons
	 * Buttons are red or green (for opened and closed doors)
	 */
	public Gui() {
		JFrame window = new JFrame("kitchen");

		a.setBounds(25, 0, 150, 250);
		a.setBackground(Color.green);

		b.setBounds(175, 0, 150, 50);
		b.setBackground(Color.green);

		c.setBounds(175, 50, 150, 200);
		c.setBackground(Color.green);

		d.setBounds(325, 0, 150, 50);
		d.setBackground(Color.green);

		e.setBounds(325, 50, 150, 200);
		e.setBackground(Color.green);
		window.setLayout(null);
		window.setBounds(0, 0, 500, 300);
		window.add(a);
		window.add(b);
		window.add(c);
		window.add(d);
		window.add(e);
		window.setVisible(true);
	}

	/**
	 * Updates the gui with the data from the doorEvent
	 * @param event doorEvent for the door that should be updated
	 */
	public void updateGui(DoorEvent event) {
		JButton buttonToModify;
		switch (event.sensor) {
		case 'a':
			buttonToModify = a;
			break;
		case 'b':
			buttonToModify = b;
			break;
		case 'c':
			buttonToModify = c;
			break;
		case 'd':
			buttonToModify = d;
			break;
		case 'e':
			buttonToModify = e;
			break;
		default:
			buttonToModify = new JButton();
		}
		buttonToModify.setBackground(event.closed? Color.green : Color.red);
	}

}
