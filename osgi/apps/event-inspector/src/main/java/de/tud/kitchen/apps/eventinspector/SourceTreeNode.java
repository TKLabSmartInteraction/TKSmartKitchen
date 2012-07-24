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
