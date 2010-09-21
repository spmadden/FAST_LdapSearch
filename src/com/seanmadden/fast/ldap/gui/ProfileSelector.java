/*
 * ProfileSelector.java
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

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.seanmadden.fast.ldap.ConnectionProfile;

/**
 * This class accepts a configuration object and allows the user to create, edit
 * and delete ldap connection profiles.
 * 
 * @author Sean P Madden
 */
public class ProfileSelector extends JFrame implements ActionListener,
		ListSelectionListener, MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836895241030077207L;

	private List<ConnectionProfile> profiles = null;
	private JList list = null;
	private JButton selectButton = null;
	private JButton delButton = null;
	private JButton editButton = null;
	private ConnectionProfile selected = null;

	public ProfileSelector(List<ConnectionProfile> profiles) {
		super("Select Connection Profile");
		this.profiles = profiles;

		this.setLayout(new BorderLayout(10, 10));

		DefaultListModel model = new DefaultListModel();
		if (this.profiles == null) {
			this.profiles = new Vector<ConnectionProfile>();
		}
		for (ConnectionProfile p : this.profiles) {
			model.addElement(p);
		}
		list = new JList(model);
		list.addListSelectionListener(this);
		list.addMouseListener(this);
		Border raisedetched = BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED);
		JScrollPane jsp = new JScrollPane(list);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBorder(raisedetched);
		this.add(jsp, BorderLayout.CENTER);

		JButton addButton = new JButton("Add Profile");
		addButton.setActionCommand("ADD");
		addButton.setMnemonic('A');
		addButton.addActionListener(this);

		editButton = new JButton("Edit Profile");
		editButton.setActionCommand("EDIT");
		editButton.setMnemonic('E');
		editButton.addActionListener(this);

		delButton = new JButton("Delete Profile");
		delButton.setActionCommand("DEL");
		delButton.setMnemonic('D');
		delButton.addActionListener(this);

		JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
		buttonsPanel.add(addButton);
		buttonsPanel.add(editButton);
		buttonsPanel.add(delButton);
		this.add(buttonsPanel, BorderLayout.EAST);

		selectButton = new JButton("Select");
		selectButton.setActionCommand("OK");
		selectButton.setMnemonic('S');
		selectButton.addActionListener(this);
		this.getRootPane().setDefaultButton(selectButton);

		selectButton.setEnabled(false);
		editButton.setEnabled(false);
		delButton.setEnabled(false);

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
		int xpos = size.width/2;
		int ypos = size.height/2;
		xpos -= this.getWidth()/2;
		ypos -= this.getHeight()/2;
		this.setLocation(xpos, ypos);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public synchronized ConnectionProfile getSelection() {
		this.setVisible(true);
		try {
			this.wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return selected;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		if (cmd.equals("OK")) {
			this.selected = (ConnectionProfile) list.getSelectedValue();
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
		} else if (cmd.equals("CANCEL")) {
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
		} else if (cmd.equals("ADD")) {
			new Thread() {
				public void run() {
					ConnectionProfile prof = new ConnectionProfile("", "", "",
							"");
					ConnectionProfileDialog cpd = new ConnectionProfileDialog(
							prof);
					cpd.waitUntilSelected();
					DefaultListModel model = (DefaultListModel) list.getModel();
					model.addElement(prof);
					profiles.add(prof);
				}
			}.start();
		} else if (cmd.equals("EDIT")) {
			new Thread() {
				public void run() {
					ConnectionProfile prof = (ConnectionProfile) list
							.getSelectedValue();
					if (prof == null) {
						return;
					}
					ConnectionProfileDialog cpd = new ConnectionProfileDialog(
							prof);
					cpd.waitUntilSelected();
					list.repaint();
				}
			}.start();
		} else if (cmd.equals("DEL")) {
			int choice = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to delete it?", "Are 'ya shure?",
					JOptionPane.YES_NO_OPTION);
			
			if(choice == JOptionPane.YES_OPTION){
				ConnectionProfile index = (ConnectionProfile)list.getSelectedValue();
				DefaultListModel model = (DefaultListModel)list.getModel();
				model.removeElement(index);
				profiles.remove(index);
				list.setSelectedIndex(-1);
			}
		}

	}
	
	public List<ConnectionProfile> getProfiles(){
		return profiles;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (!arg0.getValueIsAdjusting()) {
			selectButton.setEnabled((list.getSelectedIndex() != -1));
			editButton.setEnabled((list.getSelectedIndex() != -1));
			delButton.setEnabled((list.getSelectedIndex() != -1));
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if(arg0.getClickCount() == 2){
			if(list.getSelectedIndex() == -1){
				return;
			}
			this.selected = (ConnectionProfile) list.getSelectedValue();
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
			
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
