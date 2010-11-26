/*
 * ReportsWindow.java
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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.seanmadden.fast.ldap.reports.Report;
import com.seanmadden.fast.ldap.reports.ReportOption;
import com.seanmadden.fast.ldap.reports.ReportOptionDefaultPanel;

/**
 * This class accepts a configuration object and allows the user to create, edit
 * and delete ldap connection profiles.
 * 
 * @author Sean P Madden
 */
public class ReportsWindow extends JFrame implements ActionListener,
		ListSelectionListener, MouseListener {

	private static final long serialVersionUID = 5836895241030077207L;
	private static Logger log = Logger.getLogger(ReportsWindow.class);

	private Collection<Report> reports = null;
	private JList list = null;
	private JButton selectButton = null;
	private JPanel optionsPanel = null;
	private Report selected = null;

	public ReportsWindow(Collection<Report> reports,
			Hashtable<String, Hashtable<String, ReportOption>> reportOptions) {
		super("Select Report");
		this.reports = reports;

		this.setLayout(new BorderLayout(10, 10));

		DefaultListModel model = new DefaultListModel();
		if (this.reports == null) {
			this.reports = new Vector<Report>();
		}
		for (Report p : this.reports) {
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
		this.add(jsp, BorderLayout.WEST);

		optionsPanel = new JPanel();
		optionsPanel.setSize(300, 400);
		optionsPanel.setLayout(new BorderLayout());
		optionsPanel.add(new JLabel("Please select a report"),
				BorderLayout.CENTER);
		this.add(optionsPanel, BorderLayout.CENTER);

		selectButton = new JButton("Execute");
		selectButton.setActionCommand("OK");
		selectButton.setMnemonic('S');
		selectButton.addActionListener(this);
		this.getRootPane().setDefaultButton(selectButton);

		selectButton.setEnabled(false);
		
		JButton outputOptions = new JButton("Output");
		outputOptions.setActionCommand("OUTPUT");
		outputOptions.setMnemonic('O');
		outputOptions.addActionListener(this);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("CANCEL");
		cancelButton.setMnemonic('C');
		cancelButton.addActionListener(this);

		JPanel lowerButtonsPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		lowerButtonsPanel.add(selectButton);
		lowerButtonsPanel.add(outputOptions);
		lowerButtonsPanel.add(cancelButton);
		this.add(lowerButtonsPanel, BorderLayout.SOUTH);

		this.setSize(600, 350);

		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = size.width / 2;
		int ypos = size.height / 2;
		xpos -= this.getWidth() / 2;
		ypos -= this.getHeight() / 2;
		this.setLocation(xpos, ypos);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListener() {
			public void windowActivated(WindowEvent arg0) {
			}

			public void windowClosed(WindowEvent arg0) {
				synchronized (ReportsWindow.this) {
					ReportsWindow.this.notifyAll();
				}
			}

			public void windowClosing(WindowEvent arg0) {

			}

			public void windowDeactivated(WindowEvent arg0) {

			}

			public void windowDeiconified(WindowEvent arg0) {

			}

			public void windowIconified(WindowEvent arg0) {

			}

			public void windowOpened(WindowEvent arg0) {

			}

		});
	}

	public synchronized Report getSelection() {
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
			this.selected = (Report) list.getSelectedValue();
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
		} else if (cmd.equals("CANCEL")) {
			synchronized (this) {
				this.notifyAll();
			}
			this.dispose();
		} else if (cmd.equals("OUTPUT")){
			
		}

	}

	public Collection<Report> getProfiles() {
		return reports;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		if (!arg0.getValueIsAdjusting()) {
			selectButton.setEnabled((list.getSelectedIndex() != -1));
			// Clean out the stale listeners.
			for (ActionListener l : selectButton.getActionListeners()) {
				selectButton.removeActionListener(l);
			}
			selectButton.addActionListener(this);

			Report r = (Report) list.getSelectedValue();
			log.debug(r);
			if (r.hasOptions()) {
				log.debug(r.getOptions());
				ReportOptionDefaultPanel panel = new ReportOptionDefaultPanel(
						r.getOptions());
				selectButton.addActionListener(panel);
				optionsPanel.removeAll();
				JScrollPane jsp = new JScrollPane(panel);
				optionsPanel.add(jsp, BorderLayout.CENTER);
			} else {
				optionsPanel.removeAll();
				optionsPanel.setLayout(new BorderLayout());
				optionsPanel.add(new JLabel("No options for\r\n this report"),
						BorderLayout.CENTER);
			}
			this.invalidate();
			this.validate();
			this.repaint();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (arg0.getClickCount() == 2) {
			if (list.getSelectedIndex() == -1) {
				return;
			}
			this.selected = (Report) list.getSelectedValue();
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
