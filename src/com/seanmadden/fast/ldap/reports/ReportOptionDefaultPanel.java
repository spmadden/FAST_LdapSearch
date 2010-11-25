/*
 * XMLEditGui.java
 * 
 *    Copyright (C) 2009 Sean P Madden
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
package com.seanmadden.fast.ldap.reports;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Map.Entry;

import javax.swing.*;

/**
 * This class is designed as a dynamically changing GUI that allows the user to
 * edit configuration settings live. It provides minimal error checking.
 * 
 * I've designed this class to use a ghetto implementation of the command
 * pattern by creating action listeners that don't actually listen for actions.
 * They are going to be used as processing callbacks for the different types of
 * variables we're storing and editing here.
 * 
 * @author Sean P Madden
 */
public class ReportOptionDefaultPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -1979628049690754245L;

	/**
	 * This basic class is used simply to maintain state for two JComponenets,
	 * it's label and value editor. It is essentially used as a single row of
	 * the table we're going to build.
	 * 
	 * @author Sean P Madden
	 */
	private class TableRow {
		JComponent label;
		JComponent edit;
		String name;
		String type;
	}

	/**
	 * This is the containers of JComponents that the GUI will be using.
	 */
	private Vector<TableRow> components = new Vector<TableRow>();
	private Hashtable<String, ReportOption> config;

	/**
	 * Can't have a GUI to edit NULL!
	 * 
	 * @param config
	 */
	public ReportOptionDefaultPanel(Hashtable<String, ReportOption> config) {
		this.config = config;
	}

	/**
	 * Dynamically generates and displays a gui the user can interface with and
	 * edit this system.
	 * 
	 */
	protected void displayGUI() {
		for(Entry<String, ReportOption> ro : config.entrySet()){
			ReportOption opt = ro.getValue();
			if(opt.getType() == ReportOption.Type.String){
				makeStringValue(opt);
			}else if(opt.getType() == ReportOption.Type.Boolean){
				makeBooleanValue(opt);
			}else if(opt.getType() == ReportOption.Type.Integer){
				makeIntegerValue(opt);
			}
		}

		this.setLayout(new GridLayout(components.size() + 1, 2));
		for (TableRow comp : components) {
			this.add(comp.label);
			this.add(comp.edit);
		}
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setActionCommand("SaveXML");
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("CancelClose");

		this.add(saveButton);
		this.add(cancelButton);
	}

	private void makeIntegerValue(ReportOption ro) {
		JTextField comp = new JTextField();
		comp.setText(ro.getIntValue().toString());
		comp.setActionCommand(ro.getName());
		JLabel label = new JLabel(ro.getDescription());

		TableRow row = new TableRow();
		row.edit = comp;
		row.label = label;
		row.name = ro.getName();
		row.type = "Integer";

		components.add(row);		
	}

	private void makeBooleanValue(ReportOption ro) {
		JComboBox comp = new JComboBox(new String[] { "Yes", "No" });
		comp.setActionCommand(ro.getName());
		JLabel label = new JLabel(ro.getDescription());

		TableRow row = new TableRow();
		row.edit = comp;
		row.label = label;
		row.name = ro.getName();
		row.type = "Boolean";

		components.add(row);		
	}

	private void makeStringValue(ReportOption ro) {
		JTextField comp = new JTextField();
		comp.setText(ro.getStrValue());
		comp.setActionCommand(ro.getName());
		JLabel label = new JLabel(ro.getDescription());

		TableRow row = new TableRow();
		row.label = label;
		row.edit = comp;
		row.name = ro.getName();
		row.type = "String";

		components.add(row);
		
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("SaveXML")) {
			for (TableRow row : components) {
				ReportOption ro = config.get(row.name);
				if (row.type.equals("Integer")) {
					JTextField field = (JTextField) row.edit;
					try {
						ro.setIntValue(Integer.valueOf(field
								.getText()));
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, field.getText()
								+ " is not an integer.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else if (row.type.equals("String")) {
					JTextField field = (JTextField) row.edit;
					ro.setStrValue(field.getText());
				} else if(row.type.equals("Boolean")){
					JComboBox field = (JComboBox) row.edit;
					Boolean value = field.getSelectedItem().equals("Yes");
					ro.setBoolValue(value);
				}
			}
		} 
	}
}
