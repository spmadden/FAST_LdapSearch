/*
 * LDAPSpike2.java
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
package com.seanmadden.spikes;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import com.seanmadden.fast.ldap.*;
import com.sun.jndi.ldap.LdapCtx;

public class LDAPSpike2 {

	public static void main(String[] args) throws NamingException {
		String ldapHost = "ldap://finance.rit.edu";
		String loginDN = "cn=spmfa,OU=Support,OU=Systems & Technology,OU=Finance & Administration,dc=finance,dc=rit,dc=edu";
		String password = "Noctournicus4";
		String entryDN = "CN=bahrla,OU=Apartment Housing,OU=Finance & Administration,DC=finance,DC=rit,DC=edu";

		LdapInterface ldap = new LdapInterface(ldapHost, loginDN, "", password);
		DirContext ctx = ldap.getContext();
		
	    SearchControls sc = new SearchControls();
	    String[] attributeFilter = { "cn", "dn" };
	    sc.setReturningAttributes(attributeFilter);
	    sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
		
		NamingEnumeration<SearchResult> answer = ctx.search("dc=finance,dc=rit,dc=edu", "cn=bpf*",sc);
		while(answer.hasMore()){
			SearchResult sr = answer.next();
			entryDN = sr.getNameInNamespace();
			System.out.println(entryDN);
		}
		System.out.println();
		LdapCtx e = (LdapCtx) ctx.lookup(entryDN);
		Attributes userAtts = e.getAttributes("");

		User u = new User();
		u.setName((String) userAtts.get("displayName").get());
		u.setDescription((String) userAtts.get("description").get());
		u.setDn((String) userAtts.get("distinguishedName").get());
		u.setLastLogonHost((String) userAtts.get("userPCName").get());
		u.setLastLogonIP((String) userAtts.get("userIP").get());
		u.setLastLogonMac((String) userAtts.get("userMAC").get());

		Attribute userPrinterAtts = userAtts.get("userPrinterMap");
		if (userPrinterAtts != null) {
			NamingEnumeration<?> userPrinters = userPrinterAtts.getAll();
			while (userPrinters.hasMore()) {
				u.addPrinter((String) userPrinters.next());
			}
		}

		Attribute userShareAtts = userAtts.get("userShareMap");
		if (userShareAtts != null) {
			NamingEnumeration<?> userShares = userShareAtts.getAll();
			while (userShares.hasMore()) {
				u.addShare((String) userShares.next());
			}
		}

		NamingEnumeration<?> memberships = userAtts.get("memberOf").getAll();
		while (memberships.hasMore()) {
			String membership = (String) memberships.next();
			System.out.println(membership);
			Group g = makeGroup(membership, ctx);
			u.addGroup(g);
		}
		System.out.println(u);
	}

	public static Group makeGroup(String group, DirContext ctx)
			throws NamingException {
		Group g = new Group();
		// lookup group information.
		DirContext groupsContext = (LdapCtx) ctx.lookup(group);
		Attributes groupAttrs = groupsContext.getAttributes("");
		
		g.setName((String) groupAttrs.get("name").get());
		if(groupAttrs.get("description") != null){
			g.setDescription((String)groupAttrs.get("description").get());
		}
		g.setDn((String) groupAttrs.get("distinguishedName").get());
		
		Attribute groupShareAttrs = groupAttrs.get("groupShareMap");
		if (groupShareAttrs != null) {
			NamingEnumeration<?> shares = groupShareAttrs.getAll();
			while (shares.hasMore()) {
				g.addShare((String) shares.next());
			}
		}

		Attribute printerAtts = groupAttrs.get("groupPrinterMap");
		if (printerAtts != null) {
			NamingEnumeration<?> printers = printerAtts.getAll();

			while (printers.hasMore()) {
				g.addPrinter((String) printers.next());
			}
		}

		Attribute groupAtt = groupAttrs.get("memberOf");
		if (groupAtt != null) {
			NamingEnumeration<?> groups = groupAtt.getAll();

			while (groups.hasMore()) {
				Group gr = makeGroup((String) groups.next(), ctx);
				g.addGroup(gr);
			}
		}

		return g;
	}
}
