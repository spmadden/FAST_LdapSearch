/*
 * PasswordPrompter.java
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
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * This is a static class that allows the user to enter a password
 *
 * @author Sean P Madden
 */
public class PasswordPrompter {

	public static String promptForPassword(Object message){
		
		final StringBuffer str = new StringBuffer();
		final Object lock = new Object();
		
		final JFrame frame = new JFrame(message.toString());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(400, 100);
		frame.setLayout(new BorderLayout());
		frame.add(new JLabel(message.toString()), BorderLayout.NORTH);
		
		final JPasswordField jtf = new JPasswordField();
		frame.add(jtf, BorderLayout.CENTER);
		
		JPanel buttons = new JPanel(new FlowLayout());
		JButton okayButton = new JButton("Okay");
		okayButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				str.append(jtf.getPassword());
				synchronized(lock){
					lock.notifyAll();
				}
				frame.dispose();
			}
		});
		frame.getRootPane().setDefaultButton(okayButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				synchronized(lock){
					lock.notifyAll();
				}
				frame.dispose();
			}
		});
		
		buttons.add(okayButton);
		buttons.add(cancelButton);
		frame.add(buttons, BorderLayout.SOUTH);
		
		
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = size.width/2;
		int ypos = size.height/2;
		xpos -= frame.getWidth()/2;
		ypos -= frame.getHeight()/2;
		frame.setLocation(xpos, ypos);
		frame.setVisible(true);
		
		synchronized(lock){
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return str.toString();
	}
	
}
