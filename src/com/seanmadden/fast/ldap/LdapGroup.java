/*
 * LdapGroup.java
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

import java.util.List;
import java.util.Vector;

import javax.naming.*;

import com.sun.jndi.ldap.LdapCtx;

/**
* This object implements lazy-loading of LDAP Groups.
*
* @author Sean P Madden
*/
public class LdapGroup {
	
	private String name;
	private LdapCtx inter;
	private boolean isOu = false;
	private LdapGroup parent = null;

	public LdapGroup(LdapCtx inter, String name){
		this.inter  = inter;
		this.name = name;
	}
	
	public List<LdapUserObject> getMembers(){
		
		return null;
	}
	
	public List<LdapGroup> getSubGroups() throws NamingException{
		Vector<LdapGroup> groups = new Vector<LdapGroup>();
		String name = this.getName();
		NamingEnumeration<NameClassPair> el = inter.list(name);
		while(el.hasMore()){
			NameClassPair ncp = el.next();
			System.out.println(ncp.getName());
			LdapGroup group = new LdapGroup(inter, ncp.getName().substring(3));
			if(ncp.getName().startsWith("OU")){
				group.setOu(true);
				group.setParent(this);
			}
			groups.add(group);
		}
		return groups;
	}

	
	/**
	 * Returns the isOu
	 *
	 * @return isOu the isOu
	 */
	public boolean isOu() {
		return isOu;
	}

	/**
	 * Sets the isOu
	 *
	 * @param isOu the isOu to set
	 */
	public void setOu(boolean isOu) {
		this.isOu = isOu;
	}
	
	/**
	 * Returns the parent
	 *
	 * @return parent the parent
	 */
	public LdapGroup getParent() {
		return parent;
	}
	
	/**
	 * Sets the parent
	 *
	 * @param parent the parent to set
	 */
	public void setParent(LdapGroup parent) {
		this.parent = parent;
	}

	/**
	 * Returns the name
	 *
	 * @return name the name
	 */
	public String getName() {
		String dn = "CN=";
		if(this.isOu){
			dn="OU=";
		}
		if(this.parent != null){
			return dn+this.name + "," + this.parent.getName();
		}
		return dn+this.name;
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
		StringBuilder builder = new StringBuilder();
		builder.append("LdapGroup [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}
	
	
}
