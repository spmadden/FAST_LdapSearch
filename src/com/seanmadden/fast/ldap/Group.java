/*
* Group.java
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
package com.seanmadden.fast.ldap;

import java.util.Vector;

/**
 * This is an LDAP group for access reasons.
 *
 * @author Sean P Madden
 */
public class Group {
	private Vector<String> shares = new Vector<String>();
	private Vector<String> printers = new Vector<String>();
	private Vector<Group> groups = new Vector<Group>();
	
	private String name = "";
	private String description = "";
	private String dn = "";
	
	public void addShare(String share){
		shares.add(share);
	}
	
	public void addPrinter(String printer){
		printers.add(printer);
	}
	
	public void addGroup(Group group){
		groups.add(group);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDn() {
		return dn;
	}
	public void setDn(String dn) {
		this.dn = dn;
	}
	public Vector<String> getShares() {
		return shares;
	}
	public Vector<String> getPrinters() {
		return printers;
	}
	public Vector<Group> getGroups() {
		return groups;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Group [");
		if (shares != null) {
			builder.append("shares=");
			builder.append(shares);
			builder.append(", ");
		}
		if (printers != null) {
			builder.append("printers=");
			builder.append(printers);
			builder.append(", ");
		}
		if (groups != null) {
			builder.append("groups=");
			builder.append(groups);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (dn != null) {
			builder.append("dn=");
			builder.append(dn);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
}
