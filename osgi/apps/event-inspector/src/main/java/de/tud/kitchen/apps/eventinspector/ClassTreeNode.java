package de.tud.kitchen.apps.eventinspector;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = -671454792156600474L;

	public ClassTreeNode(Class<?> classObject) {
		super(classObject);
	}
	
	@Override
	public void setUserObject(Object userObject) {
		throw new UnsupportedOperationException("the userObject should never be changed");
	}
	
	public void add(ClassTreeNode classTreeNode) {
		Class<?> classObject = (Class<?>) classTreeNode.getUserObject();
		Class<?> superClass = classObject.getSuperclass();
		if (superClass.equals(getUserObject())) {
			if (findNodeForClassInDirectChildren(classObject) == null)
				super.add((DefaultMutableTreeNode) classTreeNode);
			return;
		}
		
		while (!superClass.equals(Object.class)) {
			if (superClass.getSuperclass().equals(getUserObject())) {
				DefaultMutableTreeNode childNode = findNodeForClassInDirectChildren(superClass);
				if (childNode == null) {
					childNode = new ClassTreeNode(superClass);
					this.add(childNode);
				}
				childNode.add(classTreeNode);
				return;
			}
			superClass = superClass.getSuperclass();
		}
	}
	
	private DefaultMutableTreeNode findNodeForClassInDirectChildren(Class<?> eventClass) {
		Enumeration<?> enumeration = children();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode element = (DefaultMutableTreeNode) enumeration.nextElement();
			if (element.getUserObject().equals(eventClass))
				return element;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return ((Class<?>) getUserObject()).getSimpleName();
	}
	
}
