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
