package de.tud.kitchen.apps.eventinspector;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;

public class EventWindow {

	private JFrame frame;
	private EventConsumer consumer;
	private JToggleButton eventStreamEnableButton;
	private JTextPane eventTextPane;
	
	/**
	 * Create the application.
	 */
	public EventWindow() {
		consumer = new DebugEventConsumer();
		initialize();
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public EventConsumer getEventConsumer() {
		return consumer;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 634, 503);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		eventStreamEnableButton = new JToggleButton("Show Events");
		panel.add(eventStreamEnableButton);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		eventTextPane = new JTextPane();
		eventTextPane.setEditable(false);
		scrollPane.setViewportView(eventTextPane);
	}
	
	public class DebugEventConsumer extends EventConsumer {
		public void handleEvent(final Event event) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					eventTextPane.setText(eventTextPane.getText() + "\n" + event.toString());
				}
			});
		}
	}

}
