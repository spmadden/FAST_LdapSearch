/*
 * ProgressBar.java
 * 
 *  $Id$
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
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import com.seanmadden.fast.ldap.reports.Report;
import com.seanmadden.fast.ldap.reports.Reports;

/**
 * This class makes an indeterminant progress bar
 * 
 * @author Sean P Madden
 */
public class ProgressBar extends JFrame {

	private class ProgressBarAppender implements Appender {

		public void addFilter(Filter arg0) {

		}

		public void clearFilters() {

		}

		public void close() {

		}

		public void doAppend(LoggingEvent arg0) {
			status.setText(arg0.getMessage().toString());
			status.repaint();
			status.validate();
		}

		public ErrorHandler getErrorHandler() {
			return null;
		}

		public Filter getFilter() {
			return null;
		}

		public Layout getLayout() {
			return null;
		}

		public String getName() {
			return null;
		}

		public boolean requiresLayout() {
			return false;
		}

		public void setErrorHandler(ErrorHandler arg0) {

		}

		public void setLayout(Layout arg0) {

		}

		public void setName(String arg0) {

		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6261060684914527484L;
	private JLabel status = new JLabel();
	private ProgressBarAppender appender = new ProgressBarAppender();
	private Level previous = null;
	
	public ProgressBar() {
		super("PROGRESS!");
		JProgressBar bar = new JProgressBar();
		bar.setIndeterminate(true);
		setLayout(new BorderLayout());
		add(bar, BorderLayout.CENTER);
		add(status, BorderLayout.NORTH);
		setSize(400, 50);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = size.width / 2;
		int ypos = size.height / 2;
		xpos -= this.getWidth() / 2;
		ypos -= this.getHeight() / 2;
		this.setLocation(xpos, ypos);
	}

	public void start() {
		Report.logger.addAppender(appender);
		previous = Report.logger.getLevel();
		Report.logger.setLevel(Level.ALL);
		setVisible(true);
	}

	public void stop() {
		Report.logger.removeAppender(appender);
		Report.logger.setLevel(previous);
		dispose();
	}

}
