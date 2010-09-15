/*
 * ConnectionProfileDialog.java
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

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.seanmadden.fast.ldap.ConnectionProfile;

/**
 * A GUI to modify the contents of the Connection Profile
 * 
 * @author Sean P Madden
 */
public class ConnectionProfileDialog extends JFrame implements ActionListener {

	private static Logger log = Logger.getLogger(ConnectionProfileDialog.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2290526373516172832L;

	/**
	 * the profile we're editing
	 */
	private ConnectionProfile profile = null;

	private JTextField name = new JTextField();
	private JTextField auth = new JTextField();
	private JTextField server = new JTextField();
	private JTextField groups = new JTextField();

	private boolean selected = false;

	/**
	 * Accepts a ConnectionProfile object
	 * 
	 * @param prof
	 */
	public ConnectionProfileDialog(ConnectionProfile prof) {
		super("Edit Connection Profile");
		this.profile = prof;
		if (prof == null) {
			this.profile = new ConnectionProfile("", "", "", "");
		}

		this.setLayout(new BorderLayout());
		JPanel upperPanel = new JPanel(new SpringLayout());
		JLabel label = new JLabel("Profile Name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		upperPanel.add(label);
		name
				.setToolTipText("upperPanel is the name the profile will be referenced by");
		name.setText(profile.getName());
		upperPanel.add(name);
		label = new JLabel("Server Auth String:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		upperPanel.add(label);
		auth
				.setToolTipText("Ex: 'cn=spmfa,OU=Support,OU=Systems &amp; Technology,OU=Finance &amp; Administration,DC=finance,DC=rit,DC=edu '");
		auth.setText(profile.getLdapAuthString());
		upperPanel.add(auth);
		label = new JLabel("Server Connection String:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		upperPanel.add(label);
		server.setToolTipText("Ex: 'ldap://finance.rit.edu'");
		server.setText(profile.getLdapServerString());
		upperPanel.add(server);
		label = new JLabel("Groups String:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		upperPanel.add(label);
		groups.setToolTipText("Ex: 'OU=Groups,DC=finance,DC=rit,DC=edu'");
		groups.setText(profile.getLdapGroupsString());
		upperPanel.add(groups);
		SpringUtilities.makeCompactGrid(upperPanel, 4, 2, 6, 6, 6, 6);
		this.add(upperPanel, BorderLayout.CENTER);

		JPanel lowerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		JButton okButton = new JButton("Okay");
		okButton.addActionListener(this);
		okButton.setMnemonic('O');
		okButton.setActionCommand("OKAY");

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setMnemonic('C');
		cancelButton.setActionCommand("CANCEL");

		lowerPanel.add(okButton);
		lowerPanel.add(cancelButton);
		this.add(lowerPanel, BorderLayout.SOUTH);

		this.setSize(500, 200);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = size.width/2;
		int ypos = size.height/2;
		xpos -= this.getWidth()/2;
		ypos -= this.getHeight()/2;
		this.setLocation(xpos, ypos);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		if (cmd.equals("OKAY")) {
			if (name.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Name cannot be blank",
						"Name cannot be blank", JOptionPane.ERROR_MESSAGE);
				return;
			}
			profile.setName(name.getText().trim());

			if (auth.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Auth cannot be blank",
						"Auth cannot be blank", JOptionPane.ERROR_MESSAGE);
				return;
			}
			profile.setLdapAuthString(auth.getText().trim());

			if (server.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Server cannot be blank",
						"Server cannot be blank", JOptionPane.ERROR_MESSAGE);
				return;
			}
			profile.setLdapServerString(server.getText().trim());

			if (groups.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this, "Groups cannot be blank",
						"Groups cannot be blank", JOptionPane.ERROR_MESSAGE);
				return;
			}
			profile.setLdapGroupsString(groups.getText().trim());
			this.selected = true;
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
		}else if(cmd.equals("CANCEL")){
			this.selected = true;
			synchronized(this){
				this.notifyAll();
			}
			this.dispose();
		}

	}

	public synchronized void waitUntilSelected() {
		try {
			while (!this.selected) {
				this.wait();
			}
		} catch (InterruptedException e) {
			log.error("Shit went down cap'm");
		}
	}

}
