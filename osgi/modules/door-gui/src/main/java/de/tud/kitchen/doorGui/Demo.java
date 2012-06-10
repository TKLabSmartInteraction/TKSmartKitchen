package de.tud.kitchen.doorGui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

import de.tud.kitchen.api.event.furniture.DoorEvent;

public class Demo {

	final JButton a = new JButton("A");
	final JButton b = new JButton("B");
	final JButton c = new JButton("C");
	final JButton d = new JButton("D");
	final JButton e = new JButton("E");

	public Demo() {
		JFrame window = new JFrame("kitchen");

		a.setBounds(0, 0, 150, 250);
		a.setBackground(Color.green);

		b.setBounds(150, 0, 150, 50);
		b.setBackground(Color.green);

		c.setBounds(150, 50, 150, 200);
		c.setBackground(Color.green);

		d.setBounds(300, 0, 150, 50);
		d.setBackground(Color.green);

		e.setBounds(300, 50, 150, 200);
		e.setBackground(Color.green);
		window.setLayout(null);
		window.setBounds(0, 0, 600, 400);
		window.add(a);
		window.add(b);
		window.add(c);
		window.add(d);
		window.add(e);
		window.setVisible(true);
	}

	public void updateGui(DoorEvent arg0) {
		JButton buttonToModify;
		switch (arg0.sensor) {
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
		buttonToModify.setBackground(arg0.closed? Color.green : Color.red);
	}

}
