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

import java.awt.*;
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
		String help;
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
		displayGUI();
	}

	/**
	 * Dynamically generates and displays a gui the user can interface with and
	 * edit this system.
	 * 
	 */
	protected void displayGUI() {
		for (Entry<String, ReportOption> ro : config.entrySet()) {
			ReportOption opt = ro.getValue();
			if (opt.getType() == ReportOption.Type.String) {
				makeStringValue(opt);
			} else if (opt.getType() == ReportOption.Type.Boolean) {
				makeBooleanValue(opt);
			} else if (opt.getType() == ReportOption.Type.Integer) {
				makeIntegerValue(opt);
			}
		}

		ImageIcon icon = new ImageIcon(getClass().getResource("icons/help.png"));

		SpringLayout l = new SpringLayout();
		this.setLayout(l);
		for (TableRow comp : components) {
			if (!comp.help.equals("")) {
				JLabel label = (JLabel) comp.label;
				label.setIcon(icon);
				label.setToolTipText(comp.help);
			}
			this.add(comp.label);
			this.add(comp.edit);
		}
		makeCompactGrid(this, components.size(), 2, 6, 6, 6, 6);
	}

	private static SpringLayout.Constraints getConstraintsForCell(int row,
			int col, Container parent, int cols) {
		SpringLayout layout = (SpringLayout) parent.getLayout();
		Component c = parent.getComponent(row * cols + col);
		return layout.getConstraints(c);
	}

	public static void makeCompactGrid(Container parent, int rows, int cols,
			int initialX, int initialY, int xPad, int yPad) {
		SpringLayout layout;
		try {
			layout = (SpringLayout) parent.getLayout();
		} catch (ClassCastException exc) {
			System.err
					.println("The first argument to makeCompactGrid must use SpringLayout.");
			return;
		}

		// Align all cells in each column and make them the same width.
		Spring x = Spring.constant(initialX);
		for (int c = 0; c < cols; c++) {
			Spring width = Spring.constant(0);
			for (int r = 0; r < rows; r++) {
				width = Spring.max(width,
						getConstraintsForCell(r, c, parent, cols).getWidth());
			}
			for (int r = 0; r < rows; r++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r,
						c, parent, cols);
				constraints.setX(x);
				constraints.setWidth(width);
			}
			x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
		}

		// Align all cells in each row and make them the same height.
		Spring y = Spring.constant(initialY);
		for (int r = 0; r < rows; r++) {
			Spring height = Spring.constant(0);
			for (int c = 0; c < cols; c++) {
				height = Spring.max(height,
						getConstraintsForCell(r, c, parent, cols).getHeight());
			}
			for (int c = 0; c < cols; c++) {
				SpringLayout.Constraints constraints = getConstraintsForCell(r,
						c, parent, cols);
				constraints.setY(y);
				constraints.setHeight(height);
			}
			y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
		}

		// Set the parent's size.
		SpringLayout.Constraints pCons = layout.getConstraints(parent);
		pCons.setConstraint(SpringLayout.SOUTH, y);
		pCons.setConstraint(SpringLayout.EAST, x);
	}

	private void makeIntegerValue(ReportOption ro) {
		JTextField comp = new JTextField(20);
		comp.setText(ro.getIntValue().toString());
		comp.setActionCommand(ro.getName());
		comp.setMinimumSize(new Dimension(100, 10));
		JLabel label = new JLabel(ro.getDescription());

		TableRow row = new TableRow();
		row.edit = comp;
		row.label = label;
		row.help = ro.getHelp();
		row.name = ro.getName();
		row.type = "Integer";

		components.add(row);
	}

	private void makeBooleanValue(ReportOption ro) {
		JComboBox comp = new JComboBox(new String[] { "Yes", "No" });
		comp.setActionCommand(ro.getName());
		comp.setMinimumSize(new Dimension(100, 10));
		JLabel label = new JLabel(ro.getDescription());
		JLabel help = new JLabel();
		if (!ro.getHelp().equals("")) {
			help = new JLabel(ro.getHelp());
		}
		TableRow row = new TableRow();
		row.edit = comp;
		row.help = ro.getHelp();
		row.label = label;
		row.name = ro.getName();
		row.type = "Boolean";

		components.add(row);
	}

	private void makeStringValue(ReportOption ro) {
		JTextField comp = new JTextField(20);
		comp.setText(ro.getStrValue());
		comp.setActionCommand(ro.getName());
		comp.setMinimumSize(new Dimension(100, 10));
		JLabel label = new JLabel(ro.getDescription());
		JLabel help = new JLabel();
		if (!ro.getHelp().equals("")) {
			help = new JLabel(ro.getHelp());
		}

		TableRow row = new TableRow();
		row.help = ro.getHelp();
		row.label = label;
		row.edit = comp;
		row.name = ro.getName();
		row.type = "String";

		components.add(row);

	}

	public void actionPerformed(ActionEvent e) {
		for (TableRow row : components) {
			ReportOption ro = config.get(row.name);
			if (row.type.equals("Integer")) {
				JTextField field = (JTextField) row.edit;
				try {
					ro.setIntValue(Integer.valueOf(field.getText()));
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, field.getText()
							+ " is not an integer.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} else if (row.type.equals("String")) {
				JTextField field = (JTextField) row.edit;
				ro.setStrValue(field.getText());
			} else if (row.type.equals("Boolean")) {
				JComboBox field = (JComboBox) row.edit;
				Boolean value = field.getSelectedItem().equals("Yes");
				ro.setBoolValue(value);
			}
		}
	}
}
