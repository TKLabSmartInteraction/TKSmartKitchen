package de.tud.kitchen.apps.eventinspector;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.tud.kitchen.api.event.Event;
import de.tud.kitchen.api.event.EventConsumer;
import de.tud.kitchen.apps.eventinspector.DynamicEventFilter.DynamicEventFilterDelegate;

public class EventWindow {

	private JFrame frame;
	private EventConsumer consumer;
	private JTextArea eventTextPane;
	private JTree tree;
	private ClassTreeNode rootTreeNode;
	private DynamicEventFilter dynamicEventFilter;
	private JToolBar toolBar;
	private final Action clearAction = new ClearAction();
	private final Action toggleScrollAction = new ToggleScrollAction();
	private JToggleButton tglbtnScroll;
	
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
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		eventTextPane = new JTextArea();
		eventTextPane.setEditable(false);
		scrollPane.setViewportView(eventTextPane);
		
		tree = new JTree();
		tree.setPreferredSize(new Dimension(160, 76));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		dynamicEventFilter = new DynamicEventFilter(new DynamicEventFilterDelegate() {
			@Override
			public void handleEvent(final Event event) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						eventTextPane.append(event.toString().concat("\n"));
					}
				});
			}
		});
		tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				for (TreePath path : arg0.getPaths()) {
					TreeNode node = (TreeNode) path.getLastPathComponent();
					TreePath[] paths = new TreePath[node.getChildCount()];
					@SuppressWarnings("unchecked")
					Enumeration<TreeNode> children = (Enumeration<TreeNode>) node.children();
					for (int i = 0; i < node.getChildCount(); i++) {
						final TreeNode child = children.nextElement();
						paths[i] = path.pathByAddingChild(child);
					}
					if (paths.length > 0) {
						if (arg0.isAddedPath(path)) {
							tree.getSelectionModel().addSelectionPaths(paths);
						} else {
							tree.getSelectionModel().removeSelectionPaths(paths);
						}
					}
				}
			}
		});
		tree.getSelectionModel().addTreeSelectionListener(dynamicEventFilter);
		rootTreeNode = new ClassTreeNode(Event.class);
		tree.setModel(new DefaultTreeModel(rootTreeNode));
		for (int i = 0; i < tree.getRowCount(); i++) {
		         tree.expandRow(i);
		}
		
		frame.getContentPane().add(tree, BorderLayout.EAST);
		
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		toolBar.add(clearAction);
		
		tglbtnScroll = new JToggleButton(toggleScrollAction);
		tglbtnScroll.setSelected(true);
		toolBar.add(tglbtnScroll);

		DefaultCaret caret = (DefaultCaret)eventTextPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public class DebugEventConsumer extends EventConsumer {
		
		private HashMap<Class<?>, ClassTreeNode> seenClasses = new HashMap<Class<?>, ClassTreeNode>();
		private HashSet<String> seenSenders = new HashSet<String>();
		
		public void handleEvent(final Event event) {
			if (!seenClasses.containsKey(event.getClass())) {
				try {
					SwingUtilities.invokeAndWait(new Runnable() {
						@Override
						public void run() {
							ClassTreeNode newTreeNode = new ClassTreeNode(event.getClass());
							rootTreeNode.add(newTreeNode);
							if (informTreeModel(newTreeNode)) {
								seenClasses.put(event.getClass(), newTreeNode);
							}
							for (int i = 0; i < tree.getRowCount(); i++) {
						         tree.expandRow(i);
							}
						}
					});
					seenSenders.add(generateIdentifier(event));
					addSender(event);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			} else if (seenSenders.add(generateIdentifier(event))) {
				addSender(event);
			}
			dynamicEventFilter.handleEvent(event);
				
		}
		
		private boolean informTreeModel(TreeNode newTreeNode) {
			TreeNode parent = newTreeNode.getParent();
			if (parent!= null) {
				((DefaultTreeModel) tree.getModel()).nodesWereInserted(parent,new int[] {parent.getIndex(newTreeNode)});
				return true;
			}
			return false;
		}

		private void addSender(final Event event) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					SourceTreeNode newSourceTreeNode = new SourceTreeNode(event.sender);
					seenClasses.get(event.getClass()).add(newSourceTreeNode);
					informTreeModel(newSourceTreeNode);
				}
			});
		}
	}
	
	private static String generateIdentifier(final Event event) {
		return String.format("%s#%s", event.getClass().getName(), event.sender);
	}

	private class ClearAction extends AbstractAction {
		private static final long serialVersionUID = -6449203531109728100L;
		public ClearAction() {
			putValue(NAME, "Clear");
			putValue(SHORT_DESCRIPTION, "Clear the event log");
		}
		public void actionPerformed(ActionEvent e) {
			eventTextPane.setText("");
		}
	}
	
	private class ToggleScrollAction extends AbstractAction {
		private static final long serialVersionUID = -6449203531109728100L;
		
		private boolean scrolling = true;
		
		public ToggleScrollAction() {
			putValue(NAME, "Scroll");
			putValue(SHORT_DESCRIPTION, "Toggle Scroll Mode");
		}
		public void actionPerformed(ActionEvent e) {
			DefaultCaret caret = (DefaultCaret)eventTextPane.getCaret();
			if (scrolling) {
				caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
				eventTextPane.setCaretPosition(eventTextPane.getCaretPosition()-1);
				scrolling = false;
			} else {
				caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
				eventTextPane.setCaretPosition(eventTextPane.getDocument().getLength());
				scrolling = true;
			}
		}
	}
}
