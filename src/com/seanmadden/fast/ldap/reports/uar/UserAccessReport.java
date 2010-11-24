/*
 * UserAttributeReport.java
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
package com.seanmadden.fast.ldap.reports.uar;

import java.util.Collection;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.seanmadden.fast.ldap.*;
import com.seanmadden.fast.ldap.reports.Report;
import com.seanmadden.fast.ldap.reports.ReportResult;
import com.seanmadden.fast.ldap.reports.Reports;
import com.sun.jndi.ldap.LdapCtx;

/**
 * This class acts as a filter/sort to determine user attributes and format
 * them.
 * 
 * @author Sean P Madden
 */
public class UserAccessReport extends Report {

	private LdapInterface inter;
	private User user = null;
	private String entryDN = "";

	public UserAccessReport() {
		this.inter = Reports.getInstance().getLdapInterface();
	}

	public Collection<ReportResult> getResult() {
		return null;
	}

	public void execute() {
		DirContext ctx = inter.getContext();

		SearchControls sc = new SearchControls();
		String[] attributeFilter = { "cn", "dn" };
		sc.setReturningAttributes(attributeFilter);
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

		try {
			NamingEnumeration<SearchResult> answer = ctx.search(
					"dc=finance,dc=rit,dc=edu", "cn=bpf*", sc);
			while (answer.hasMore()) {
				SearchResult sr = answer.next();
				entryDN = sr.getNameInNamespace();
				System.out.println(entryDN);
			}
			System.out.println();
		} catch (NamingException e) {
			logger.error(e);
		}

		try {
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

			NamingEnumeration<?> memberships = userAtts.get("memberOf")
					.getAll();
			while (memberships.hasMore()) {
				String membership = (String) memberships.next();
				System.out.println(membership);
				Group g = makeGroup(membership, ctx);
				u.addGroup(g);
			}

			user = u;
		} catch (NamingException e) {
			logger.error(e);
		}

	}

	public static Group makeGroup(String group, DirContext ctx)
			throws NamingException {
		Group g = new Group();
		// lookup group information.
		DirContext groupsContext = (LdapCtx) ctx.lookup(group);
		Attributes groupAttrs = groupsContext.getAttributes("");

		g.setName((String) groupAttrs.get("name").get());
		if (groupAttrs.get("description") != null) {
			g.setDescription((String) groupAttrs.get("description").get());
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
