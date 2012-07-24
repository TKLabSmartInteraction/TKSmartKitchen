/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * 
 * Contributor(s):
 *    Marcus Staender <staender@tk.informatik.tu-darmstadt.de>
 *    Aristotelis Hadjakos <telis@tk.informatik.tu-darmstadt.de>
 *    Niklas Lochschmidt <nlochschmidt@gmail.com>
 *    Christian Klos <christian.klos@stud.tu-darmstadt.de>
 *    Bastian Renner <bastian.renner@stud.tu-darmstadt.de>
 *
 */

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
				ClassTreeNode childNode = findNodeForClassInDirectChildren(superClass);
				if (childNode == null) {
					childNode = new ClassTreeNode(superClass);
					//recursion
					this.add(childNode);
				}
				childNode.add(classTreeNode);
				return;
			}
			superClass = superClass.getSuperclass();
		}
	}
	
	private ClassTreeNode findNodeForClassInDirectChildren(Class<?> eventClass) {
		Enumeration<?> enumeration = children();
		while (enumeration.hasMoreElements()) {
			DefaultMutableTreeNode element = (DefaultMutableTreeNode) enumeration.nextElement();
			if (element.getUserObject().equals(eventClass))
				return (ClassTreeNode) element;
		}
		return null;
	}
	
	@Override
	public String toString() {
		return ((Class<?>) getUserObject()).getSimpleName();
	}
	
}
