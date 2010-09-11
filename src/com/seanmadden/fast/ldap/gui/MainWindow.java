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

import javax.swing.JFrame;

import org.apache.log4j.Logger;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class MainWindow extends JFrame{
	private static Logger log = Logger.getLogger(MainWindow.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3621989303299216642L;
	
	public MainWindow(){
		super("FAST/ITS Ldap Group Aggregator");
		log.debug("Main Window Starting");
		
		
		
		this.setSize(400, 600);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		
	}
}
