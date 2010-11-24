/*
 * LDAPSpike.java
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

import java.io.UnsupportedEncodingException;
import java.util.*;

import com.novell.ldap.*;

public class LDAPSpike {
	public static void main(String[] args) {
		System.err.println("Example: java GetAttributeSchema Acme.com"
				+ " \"cn=admin,o=Acme\" secret \"cn=john,o=Acme\"");

		int ldapPort = LDAPConnection.DEFAULT_PORT;
		int ldapVersion = LDAPConnection.LDAP_V3;
		int searchScope = LDAPConnection.SCOPE_BASE;
		String ldapHost = "finance.rit.edu";
		String loginDN = "cn=spmfa,OU=Support,OU=Systems & Technology,OU=Finance & Administration,dc=finance,dc=rit,dc=edu";
		String password = "Noctournicus4";
		String entryDN = "cn=spmfa,OU=Support,OU=Systems & Technology,OU=Finance & Administration,dc=finance,dc=rit,dc=edu";
		String searchFilter = "Objectclass=*";
		LDAPSchema dirSchema = null;

		LDAPConnection lc = new LDAPConnection();

		try {

			// connect to the server
			lc.connect(ldapHost, ldapPort);
			// bind to the server
			lc.bind(ldapVersion, loginDN, password.getBytes());
			try {
				dirSchema = lc.fetchSchema(lc.getSchemaDN());
			} catch (LDAPException e) {
				e.printStackTrace();
			}
			LDAPSearchResults searchResults = lc.search(entryDN, searchScope,
					searchFilter, null, // return all attributes
					false); // return attrs and values
			LDAPEntry Entry = null;
			try {
				Entry = searchResults.next();
			} catch (LDAPException e) {
				e.printStackTrace();
			}
			LDAPAttributeSet attributeSet = Entry.getAttributeSet();
			Iterator allAttributes = attributeSet.iterator();
			while (allAttributes.hasNext()) {
				LDAPAttribute attribute = (LDAPAttribute) allAttributes.next();
				String attributeName = attribute.getName();
				System.out.println("    "
						+ attributeName
						+ "      "
						+ dirSchema.getAttributeSchema(attributeName)
								.toString());
				System.out.println("\n");
			}
		} catch (LDAPException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
