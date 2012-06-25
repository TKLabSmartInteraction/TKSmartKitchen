package de.tud.kitchen.apps.eventinspector;

import java.util.HashSet;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import de.tud.kitchen.api.event.Event;

public class DynamicEventFilter implements TreeSelectionListener {

	private DynamicEventFilterDelegate delegate;
	
	private HashSet<String> allowedEventSender = new HashSet<String>();
	
	public DynamicEventFilter(DynamicEventFilterDelegate delegate) {
		this.delegate = delegate;
	}
	
	public void handleEvent(final Event event) {
		synchronized (allowedEventSender) {
			if (allowedEventSender.contains(generateIdentifier(event))) {
				delegate.handleEvent(event);
			}	
		}
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		synchronized (allowedEventSender) {
			for (TreePath path : e.getPaths()) {
				if (e.isAddedPath(path))
					this.addedPath(path);
				else
					this.removedPath(path);
			}
		}
	}
	
	private static String generateIdentifier(final Event event) {
		return String.format("%s#%s", event.getClass().getName(), event.sender);
	}
	
	private static String generateIdentifier(final Class<?> cls, final String sender) {
		return String.format("%s#%s", cls.getName(), sender);
	}


	public void addedPath(TreePath path) {
		synchronized (allowedEventSender) {
			final Object lastPathComponent = path.getLastPathComponent();
			if (lastPathComponent instanceof SourceTreeNode) {	
				final SourceTreeNode sourceTreeNode = (SourceTreeNode) lastPathComponent;
				allowedEventSender.add(generateIdentifier((Class<?>) ((ClassTreeNode) sourceTreeNode.getParent()).getUserObject(), (String) (sourceTreeNode.getUserObject())));
			}
		}
	}
	
	public void removedPath(TreePath path) {
		synchronized (allowedEventSender) {	
			final Object lastPathComponent = path.getLastPathComponent();
			if (lastPathComponent instanceof SourceTreeNode) {			
				final SourceTreeNode sourceTreeNode = (SourceTreeNode) lastPathComponent;
				allowedEventSender.remove(generateIdentifier((Class<?>) ((ClassTreeNode) sourceTreeNode.getParent()).getUserObject(), (String) (sourceTreeNode.getUserObject())));
			}
		}
	}
		
	public interface DynamicEventFilterDelegate {
		public void handleEvent(Event event);
	}
}
