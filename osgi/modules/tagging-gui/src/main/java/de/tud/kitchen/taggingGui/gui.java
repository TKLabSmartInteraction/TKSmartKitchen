package de.tud.kitchen.taggingGui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.tagging.TaggingEvent;

public class gui {

	JFrame window;
	JTextField input;
	JButton send;
	EventPublisher<TaggingEvent> publisher;
	
	
	public static void main(String[] args) {
		new gui(null);
	}
	public gui(EventPublisher<TaggingEvent> publisher) {
		this.publisher = publisher;
		window = new JFrame();
		window.setBounds(50, 50, 500, 100);
		input = new JTextField();
		input.setBounds(5, 10, 400, 35);
		send = new JButton("send event");
		send.setBounds(410, 10, 85, 35);
		input.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendEvent();				
			}
		});
		send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sendEvent();				
			}
		});
		window.addWindowListener(new WindowListener() {			
			@Override
			public void windowOpened(WindowEvent e) {				
			}			
			@Override
			public void windowIconified(WindowEvent e) {
			}			
			@Override
			public void windowDeiconified(WindowEvent e) {
			}			
			@Override
			public void windowDeactivated(WindowEvent e) {
			}			
			@Override
			public void windowClosing(WindowEvent e) {
				window.dispose();				
			}			
			@Override
			public void windowClosed(WindowEvent e) {
			}			
			@Override
			public void windowActivated(WindowEvent e) {		
			}
		});
		window.setLayout(null);
		window.add(input);
		window.add(send);
		window.setVisible(true);
	}
	
	private void sendEvent(){
		TaggingEvent event = new TaggingEvent("tagging-Gui", input.getText());
		input.setText("");
		publisher.publish(event);
	}
	
	public void close(){
		this.window.dispose();
	}
	
	
}
