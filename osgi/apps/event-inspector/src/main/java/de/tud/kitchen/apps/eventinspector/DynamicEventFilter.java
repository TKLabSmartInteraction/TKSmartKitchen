package de.tud.kitchen.apps.eventinspector;

import java.util.HashSet;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.tud.kitchen.api.event.Event;

public class DynamicEventFilter implements TreeSelectionListener {

	private DynamicEventFilterDelegate delegate;
	
	private HashSet<Class<?>> allowedEventClasses = new HashSet<Class<?>>();
	
	public DynamicEventFilter(DynamicEventFilterDelegate delegate) {
		this.delegate = delegate;
	}
	
	void handleEvent(final Event event) {
		synchronized (allowedEventClasses) {
			if (allowedEventClasses.contains(event.getClass())) {
				delegate.handleEvent(event);
			}	
		}
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		synchronized (allowedEventClasses) {
			for (TreePath path : e.getPaths()) {
				if (e.isAddedPath(path))
					this.addedPath(path);
				else
					this.removedPath(path);
			}
		}
	}

	private void addedPath(TreePath path) {
		allowedEventClasses.add((Class<?>) ((ClassTreeNode) path.getLastPathComponent()).getUserObject());
	}
	
	private void removedPath(TreePath path) {
		allowedEventClasses.remove((Class<?>) ((ClassTreeNode) path.getLastPathComponent()).getUserObject());
	}
		
	public interface DynamicEventFilterDelegate {
		public void handleEvent(Event event);
	}
}
