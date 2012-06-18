package de.tud.kitchen.apps.eventinspector;

import javax.swing.tree.DefaultMutableTreeNode;

public class SourceTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -671454792123600474L;

	public SourceTreeNode(String senderId) {
		super(senderId, false);
	}
	
	@Override
	public void setUserObject(Object userObject) {
		throw new UnsupportedOperationException("the userObject should never be changed");
	}
	
	@Override
	public String toString() {
		return (String) getUserObject();
	}
	
}
