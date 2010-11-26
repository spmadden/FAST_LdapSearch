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

import java.util.*;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;

import com.seanmadden.fast.ldap.*;
import com.seanmadden.fast.ldap.reports.Report;
import com.seanmadden.fast.ldap.reports.ReportOption;
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

	protected class UARResult extends ReportResult {

		{
			setForCn(user.getName());
			setForDn(user.getDn());
			setReportName(getName());
			setRunByDn(inter.getAuthDN());
			setForName(user.getUsername());
		}

		public Collection<String> getSections() {
			Vector<String> sections = new Vector<String>();
			sections.add("Detailed User Information");
			sections.add("Printers");
			sections.add("Shares");
			sections.add("Groups");
			return sections;
		}

		public Collection<String> getSectionHeaders(String section) {
			Vector<String> headers = new Vector<String>();
			if (section.equals("Detailed User Information")) {
				headers.add("Property");
				headers.add("Value");
			} else if (section.equals("Printers")) {
				headers.add("Printer");
				headers.add("Reason");
			} else if (section.equals("Shares")) {
				headers.add("Share");
				headers.add("Reason");
			} else if (section.equals("Groups")) {
				headers.add("Group Membership");
			}
			return headers;
		}

		public Collection<String[]> getSectionFields(String section) {
			Vector<String[]> fields = new Vector<String[]>();
			if (section.equals("Detailed User Information")) {
				String[] U_NAME = new String[] { "Username", user.getName() };
				String[] U_DN = new String[] { "DistinguishedName",
						user.getDn() };
				String[] U_DESC = new String[] { "Description",
						user.getDescription() };
				String[] U_HOST = new String[] { "LastLogonHost",
						user.getLastLogonHost() };
				String[] U_IP = new String[] { "LastLogonIP",
						user.getLastLogonIP() };
				String[] U_MAC = new String[] { "LastLogonMAC",
						user.getLastLogonMac() };
				fields.add(U_NAME);
				fields.add(U_DN);
				fields.add(U_DESC);
				fields.add(U_HOST);
				fields.add(U_IP);
				fields.add(U_MAC);
			} else if (section.equals("Printers")) {
				for (String printer : user.getDirectPrinters()) {
					String[] p = new String[] { printer, "direct access" };
					fields.add(p);
				}
				LinkedList<Group> queue = new LinkedList<Group>();
				queue.addAll(user.getGroups());
				while (queue.size() > 0) {
					Group g = queue.pop();
					for (String printer : g.getPrinters()) {
						String[] p = new String[] { printer,
								"Member of " + g.getName() };
						fields.add(p);
					}
					queue.addAll(g.getGroups());
				}
			} else if (section.equals("Shares")) {
				for (String share : user.getDirectShares()) {
					String[] s = new String[] { share, "direct access" };
					fields.add(s);
				}
				LinkedList<Group> queue = new LinkedList<Group>();
				queue.addAll(user.getGroups());
				while (queue.size() > 0) {
					Group g = queue.pop();
					for (String share : g.getShares()) {
						String[] s = new String[] { share,
								"Member of " + g.getName() };
						fields.add(s);
					}
					queue.addAll(g.getGroups());
				}
			} else if (section.equals("Groups")) {
				LinkedList<Group> queue = new LinkedList<Group>();
				queue.addAll(user.getGroups());
				while (queue.size() > 0) {
					Group g = queue.pop();
					String[] p = new String[] { g.getName() };
					fields.add(p);
					queue.addAll(g.getGroups());
				}
			}
			return fields;
		}
	}

	private Hashtable<String, ReportOption> options = new Hashtable<String, ReportOption>() {
		private static final long serialVersionUID = 2631350783253544448L;
		{
			put("CN", new ReportOption("CN", "Username",
					"The username to search for", "spmfa"));
			put("INC_PRINT", new ReportOption("INC_PRINT", "Include Printers",
					"Include printer access in the report", true));
			put("INC_SHARE", new ReportOption("INC_SHARE", "Include Shares",
					"Include share access in the report", true));
			put("INC_PC", new ReportOption("INC_PC", "Include PC",
					"Include last-logon hostname", true));
			put("INC_MAC", new ReportOption("INC_MAC", "Include MAC",
					"Include last-logon MAC", true));
			put("INC_IP", new ReportOption("INC_IP", "Include IP",
					"Include last-logon IP", true));
			put("RECURSE", new ReportOption("RECURSE", "Recurse groups",
					"Include all parent's group accesses", true));
		}

	};

	public UserAccessReport() {
		this.inter = Reports.getInstance().getLdapInterface();
	}

	public ReportResult getResult() {
		ReportResult res = new UARResult();
		return res;
	}

	/**
	 * Returns the cannonical name of this report.
	 * 
	 * @see com.seanmadden.fast.ldap.reports.Report#getName()
	 * @return
	 */
	public String getName() {
		return "User Access Report";
	}

	/**
	 * Returns whether or not this report has options. In this case, returns
	 * true.
	 * 
	 * @see com.seanmadden.fast.ldap.reports.Report#hasOptions()
	 * @return True.
	 */
	public boolean hasOptions() {
		return options.size() > 0;
	}

	/**
	 * Returns a list of the available options for this report.
	 * 
	 * @see com.seanmadden.fast.ldap.reports.Report#getOptions()
	 * @return List of valid options for this report.
	 */
	@Override
	public Hashtable<String, ReportOption> getOptions() {
		return options;
	}

	public void execute() {
		logger.debug(options);
		DirContext ctx = inter.getContext();

		SearchControls sc = new SearchControls();
		String[] attributeFilter = { "cn", "dn" };
		sc.setReturningAttributes(attributeFilter);
		sc.setSearchScope(SearchControls.SUBTREE_SCOPE);

		try {
			NamingEnumeration<SearchResult> answer = ctx.search(
					"dc=finance,dc=rit,dc=edu", "cn="
							+ options.get("CN").getStrValue(), sc);
			while (answer.hasMore()) {
				SearchResult sr = answer.next();
				entryDN = sr.getNameInNamespace();
				logger.debug(entryDN);
			}
			System.out.println();
		} catch (NamingException e) {
			logger.debug(e);
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
			u.setUsername((String)userAtts.get("cn").get());

			if (options.get("INC_PRINT").getBoolValue()) {
				Attribute userPrinterAtts = userAtts.get("userPrinterMap");
				if (userPrinterAtts != null) {
					NamingEnumeration<?> userPrinters = userPrinterAtts
							.getAll();
					while (userPrinters.hasMore()) {
						String printer = (String) userPrinters.next();
						u.addPrinter(printer);
						logger.info("Found Printer: " + printer);
					}
				}
			}

			if (options.get("INC_SHARE").getBoolValue()) {
				Attribute userShareAtts = userAtts.get("userShareMap");
				if (userShareAtts != null) {
					NamingEnumeration<?> userShares = userShareAtts.getAll();
					while (userShares.hasMore()) {
						String share = (String) userShares.next();
						u.addShare(share);
						logger.info("Found Share: " + share);
					}
				}
			}

			NamingEnumeration<?> memberships = userAtts.get("memberOf")
					.getAll();
			while (memberships.hasMore()) {
				String membership = (String) memberships.next();
				logger.info("Found group: " + membership);
				Group g = makeGroup(membership, ctx);
				u.addGroup(g);
			}

			user = u;
		} catch (NamingException e) {
			logger.error(e);
		}

	}

	public Group makeGroup(String group, DirContext ctx) throws NamingException {
		Group g = new Group();
		// lookup group information.
		DirContext groupsContext = (LdapCtx) ctx.lookup(group);
		Attributes groupAttrs = groupsContext.getAttributes("");

		g.setName((String) groupAttrs.get("name").get());
		if (groupAttrs.get("description") != null) {
			g.setDescription((String) groupAttrs.get("description").get());
		}
		g.setDn((String) groupAttrs.get("distinguishedName").get());

		if (options.get("INC_SHARE").getBoolValue()) {
			Attribute groupShareAttrs = groupAttrs.get("groupShareMap");
			if (groupShareAttrs != null) {
				NamingEnumeration<?> shares = groupShareAttrs.getAll();
				while (shares.hasMore()) {
					String share = (String) shares.next();
					g.addShare(share);
					logger.info("Found share: " + share);
				}
			}
		}

		if (options.get("INC_PRINT").getBoolValue()) {
			Attribute printerAtts = groupAttrs.get("groupPrinterMap");
			if (printerAtts != null) {
				NamingEnumeration<?> printers = printerAtts.getAll();

				while (printers.hasMore()) {
					String printer = (String) printers.next();
					g.addPrinter(printer);
					logger.info("Found printer: " + printer);
				}
			}
		}

		if (options.get("RECURSE").getBoolValue()) {
			Attribute groupAtt = groupAttrs.get("memberOf");
			if (groupAtt != null) {
				NamingEnumeration<?> groups = groupAtt.getAll();

				while (groups.hasMore()) {
					String str = (String) groups.next();
					logger.info("Found group: " + str);
					Group gr = makeGroup(str, ctx);
					g.addGroup(gr);
				}
			}
		}
		return g;
	}

}
