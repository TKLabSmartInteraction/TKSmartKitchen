package de.tud.kitchen.taggingGui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import de.tud.kitchen.api.event.EventPublisher;
import de.tud.kitchen.api.tagging.TaggingEvent;
import java.awt.FlowLayout;

public class gui {

	private HashSet<String> seenTags = new HashSet<String>();
	JFrame window;
	JTextField input;
	JButton send;
	EventPublisher<TaggingEvent> publisher;
	private JPanel buttonPanel;
	
	
	public static void main(String[] args) {
		new gui(null);
	}
	public gui(EventPublisher<TaggingEvent> publisher) {
		this.publisher = publisher;
		window = new JFrame();
		window.setBounds(50, 50, 500, 100);
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
		window.getContentPane().setLayout(new BorderLayout(0, 0));
		
		buttonPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		window.getContentPane().add(buttonPanel);
		
		JPanel topPanel = new JPanel();
		window.getContentPane().add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		send = new JButton("send event");
		topPanel.add(send, BorderLayout.EAST);
		input = new JTextField();
		topPanel.add(input);
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
		window.setVisible(true);
	}
	
	private void sendEvent(){
		final String text = input.getText();
		input.setText("");
		sendTaggingEvent(text);
		if (seenTags.add(text)) {
			buttonPanel.add(new JButton(new AbstractAction(text) {	
				@Override
				public void actionPerformed(ActionEvent arg0) {
					sendTaggingEvent(text);
				}
			}));
			buttonPanel.revalidate();
			buttonPanel.repaint();
		}
	}
	private void sendTaggingEvent(final String tag) {
		TaggingEvent event = new TaggingEvent("tagging-Gui", tag);
		publisher.publish(event);
	}
	
	public void close(){
		this.window.dispose();
	}
}
