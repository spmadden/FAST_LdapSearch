/*
 * ConnectionProfile.java
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

package com.seanmadden.fast.ldap;

/**
 * This class represents a single connection profile.
 * 
 * @author Sean P Madden
 */
public class ConnectionProfile {
	
	/**
	 * ldap://finance.rit.edu
	 */
	private String ldapServerString = "";
	
	/**
	 * cn=spmfa,OU=Support,OU=Systems &amp; Technology,OU=Finance &amp; Administration,DC=finance,DC=rit,DC=edu 
	 */
	private String ldapAuthString = "";
	
	/**
	 * OU=Groups,DC=finance,DC=rit,DC=edu
	 */
	private String ldapGroupsString = "";
	
	/**
	 * Finance LDAP Server
	 */
	private String name = "";

	public ConnectionProfile(String name, String ldapServerString, String ldapAuthString,
			String ldapGroupsString) {
		this.name = name;
		this.ldapServerString = ldapServerString;
		this.ldapAuthString = ldapAuthString;
		this.ldapGroupsString = ldapGroupsString;
	}

	/**
	 * Returns the ldapServerString
	 * 
	 * @return ldapServerString the ldapServerString
	 */
	public String getLdapServerString() {
		return ldapServerString;
	}

	/**
	 * Sets the ldapServerString
	 * 
	 * @param ldapServerString
	 *            the ldapServerString to set
	 */
	public void setLdapServerString(String ldapServerString) {
		this.ldapServerString = ldapServerString;
	}

	/**
	 * Returns the ldapAuthString
	 * 
	 * @return ldapAuthString the ldapAuthString
	 */
	public String getLdapAuthString() {
		return ldapAuthString;
	}

	/**
	 * Sets the ldapAuthString
	 * 
	 * @param ldapAuthString
	 *            the ldapAuthString to set
	 */
	public void setLdapAuthString(String ldapAuthString) {
		this.ldapAuthString = ldapAuthString;
	}

	/**
	 * Returns the ldapGroupsString
	 * 
	 * @return ldapGroupsString the ldapGroupsString
	 */
	public String getLdapGroupsString() {
		return ldapGroupsString;
	}

	/**
	 * Sets the ldapGroupsString
	 * 
	 * @param ldapGroupsString
	 *            the ldapGroupsString to set
	 */
	public void setLdapGroupsString(String ldapGroupsString) {
		this.ldapGroupsString = ldapGroupsString;
	}

	/**
	 * Returns the name
	 *
	 * @return name the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		return this.name;
	}



}
