/*
 * MainWindow.java
 *
 * $Id $
 * 
 *    Copyright (C) 2010 Sean P Madden
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *    If you would like to license this code under the GNU LGPL, please see
 *    http://www.seanmadden.net/licensing for details.
 *
 */

package com.seanmadden.fast.ldap.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.*;

import org.apache.log4j.Logger;

import com.seanmadden.fast.ldap.ConnectionProfile;
import com.seanmadden.fast.ldap.LdapGroup;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class MainWindow extends JFrame implements MouseListener,
		ActionListener, TreeWillExpandListener, TreeExpansionListener {
	private static Logger log = Logger.getLogger(MainWindow.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3621989303299216642L;

	private JTree list;

	private JButton selectButton;

	private List<LdapGroup> groups = null;

	public MainWindow(List<LdapGroup> groups) {
		super("FAST/ITS Ldap Group Aggregator");

		this.groups = groups;

		this.setLayout(new BorderLayout(10, 10));

		DefaultMutableTreeNode model = new DefaultMutableTreeNode("Groups");
		list = new JTree(model);

		for (LdapGroup g : groups) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(g);
			if(g.isOu()){
				node.setAllowsChildren(true);
				DefaultMutableTreeNode sub = new DefaultMutableTreeNode("Loading...");
				TreePath path = new TreePath(node.getPath());
				node.add(sub);
				list.collapsePath(path);
			}
			model.add(node);
		}

		list.addTreeExpansionListener(this);
		list.addTreeWillExpandListener(this);
		list.addMouseListener(this);
		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);
		JScrollPane jsp = new JScrollPane(list);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBorder(raisedetched);
		this.add(jsp, BorderLayout.CENTER);

		selectButton = new JButton("Select");
		selectButton.setActionCommand("OK");
		selectButton.setMnemonic('S');
		selectButton.addActionListener(this);
		this.getRootPane().setDefaultButton(selectButton);

		selectButton.setEnabled(false);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.setMnemonic('C');
		cancelButton.addActionListener(this);

		JPanel lowerButtonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		lowerButtonsPanel.add(selectButton);
		lowerButtonsPanel.add(cancelButton);
		this.add(lowerButtonsPanel, BorderLayout.SOUTH);

		this.setSize(400, 200);

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = size.width / 2;
		int ypos = size.height / 2;
		xpos -= this.getWidth() / 2;
		ypos -= this.getHeight() / 2;
		this.setLocation(xpos, ypos);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 * @param arg0
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 * @param arg0
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 * @param arg0
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 * @param arg0
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 * @param arg0
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @param arg0
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		if (cmd.equals("CANCEL")) {
			this.dispose();
		}

	}

	/**
	 * [Place method description here]
	 * 
	 * @see javax.swing.event.TreeWillExpandListener#treeWillCollapse(javax.swing.event.TreeExpansionEvent)
	 * @param arg0
	 * @throws ExpandVetoException
	 */
	@Override
	public void treeWillCollapse(TreeExpansionEvent arg0)
			throws ExpandVetoException {
		// TODO Auto-generated method stub

	}

	/**
	 * [Place method description here]
	 * 
	 * @see javax.swing.event.TreeWillExpandListener#treeWillExpand(javax.swing.event.TreeExpansionEvent)
	 * @param arg0
	 * @throws ExpandVetoException
	 */
	@Override
	public void treeWillExpand(TreeExpansionEvent arg0)
			throws ExpandVetoException {
		TreePath path = arg0.getPath();
		if(path.getPathCount() == 1){
			System.out.println(path.getLastPathComponent());
			return;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		LdapGroup group = (LdapGroup)node.getUserObject();
		try {
			node.removeAllChildren();
			for(LdapGroup g : group.getSubGroups()){
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(g);
				if(g.isOu()){
					node.setAllowsChildren(true);
					DefaultMutableTreeNode sub = new DefaultMutableTreeNode("Loading...");
					TreePath end = new TreePath(node.getPath());
					newNode.add(sub);
					list.collapsePath(end);
				}
				node.add(newNode);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * [Place method description here]
	 * 
	 * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
	 * @param arg0
	 */
	@Override
	public void treeCollapsed(TreeExpansionEvent arg0) {

	}

	/**
	 * [Place method description here]
	 * 
	 * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
	 * @param arg0
	 */
	@Override
	public void treeExpanded(TreeExpansionEvent arg0) {

	}
}
