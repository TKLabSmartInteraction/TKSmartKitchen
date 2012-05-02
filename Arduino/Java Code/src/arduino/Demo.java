package arduino;


import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Demo {
	
	public static void main(String[]args){
	JFrame window = new JFrame("kitchen");
	
	final JButton a = new JButton("A");
	a.setBounds(0, 0, 150, 250);
	a.setBackground(Color.green);
	
	final JButton b = new JButton("B");
	b.setBounds(150, 0, 150, 50);
	b.setBackground(Color.green);
	
	final JButton c = new JButton("C");
	c.setBounds(150, 50, 150, 200);
	c.setBackground(Color.green);
	
	final JButton d = new JButton("D");
	d.setBounds(300, 0, 150, 50);
	d.setBackground(Color.green);
	
	final JButton e = new JButton("E");
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
	Arduino ard = Arduino.getInstance();
	ard.addSensorEventListener(new SensorEventListener() {
		
		@Override
		public void SensorEvent(SensorEvent arg0) {
			JButton buttonToModify;
			switch (arg0.getSensor()) {
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
			buttonToModify.setBackground(arg0.getValue() ? Color.green : Color.red);
			
		}
	});
	ard.connect("COM20");
	System.out.println("started");
	try {
		Thread.sleep(1000);
	} catch (InterruptedException error) {
		// TODO Auto-generated catch block
		error.printStackTrace();
	}
	try {
		System.out.println("State of Sensor A is: "+ard.getValue(Arduino.SENSORA));
	} catch (WrongModeSelected e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if (ard.switchMode(Arduino.MCHANGES)) System.out.println("mode changed");
	
	}
	
}
