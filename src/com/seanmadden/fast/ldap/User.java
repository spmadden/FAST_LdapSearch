/*
* User.java
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

import java.util.*;

/**
 * Represents a single user and their access.
 *
 * @author Sean P Madden
 */
public class User{
	private Vector<String> shares = new Vector<String>();
	private Vector<String> printers = new Vector<String>();
	private Vector<Group> groups = new Vector<Group>();
	
	private String name = "";
	private String description = "";
	private String dn = "";
	
	private String lastLogonIP = "";
	private String lastLogonHost = "";
	private String lastLogonMac = "";
	
	public void addShare(String share){
		shares.add(share);
	}
	public void addPrinter(String printer){
		printers.add(printer);
	}
	
	public void addGroup(Group group){
		groups.add(group);
	}
	
	public Collection<String> getShares(){
		return shares;
	}
	
	public Collection<String> getPrinters(){
		return printers;
	}
	
	public Collection<Group> getGroups(){
		return groups;
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
	public String getLastLogonIP() {
		return lastLogonIP;
	}
	public void setLastLogonIP(String lastLogonIP) {
		this.lastLogonIP = lastLogonIP;
	}
	public String getLastLogonHost() {
		return lastLogonHost;
	}
	public void setLastLogonHost(String lastLogonHost) {
		this.lastLogonHost = lastLogonHost;
	}
	public String getLastLogonMac() {
		return lastLogonMac;
	}
	public void setLastLogonMac(String lastLogonMac) {
		this.lastLogonMac = lastLogonMac;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [");
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
			builder.append(", ");
		}
		if (lastLogonIP != null) {
			builder.append("lastLogonIP=");
			builder.append(lastLogonIP);
			builder.append(", ");
		}
		if (lastLogonHost != null) {
			builder.append("lastLogonHost=");
			builder.append(lastLogonHost);
			builder.append(", ");
		}
		if (lastLogonMac != null) {
			builder.append("lastLogonMac=");
			builder.append(lastLogonMac);
		}
		builder.append("]");
		return builder.toString();
	}
	
	
}
