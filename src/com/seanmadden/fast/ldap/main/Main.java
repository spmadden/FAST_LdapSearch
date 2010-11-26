/*
 * Main.java
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

package com.seanmadden.fast.ldap.main;

import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import org.apache.commons.cli.*;
import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import com.seanmadden.fast.ldap.*;
import com.seanmadden.fast.ldap.gui.*;
import com.seanmadden.fast.ldap.reports.Report;
import com.seanmadden.fast.ldap.reports.ReportOption;
import com.seanmadden.fast.ldap.reports.Reports;

/**
 * [Insert class description here]
 * 
 * @author Sean P Madden
 */
public class Main {
	private static Logger log = Logger.getLogger(Main.class);

	/**
	 * The Main Event.
	 * 
	 * @param args
	 */
	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		BasicConfigurator.configure();
		log.debug("System initializing");
		try {
			Logger.getRootLogger().addAppender(
					new FileAppender(new PatternLayout(
							PatternLayout.TTCC_CONVERSION_PATTERN), "log.log"));
		} catch (IOException e1) {
			log.error("Unable to open the log file..?");
		}

		/*
		 * Initialize the configuration engine.
		 */
		XMLConfiguration config = new XMLConfiguration();
		config.setListDelimiter('|');

		/*
		 * Initialize the Command-Line parsing engine.
		 */
		CommandLineParser parser = new PosixParser();
		Options opts = new Options();
		opts.addOption(OptionBuilder.withLongOpt("config-file").hasArg()
				.withDescription("The configuration file to load")
				.withArgName("config.xml").create("c"));

		opts.addOption(OptionBuilder.withLongOpt("profile").hasArg()
				.withDescription("The profile to use").withArgName("Default")
				.create("p"));

		opts.addOption(OptionBuilder.withLongOpt("password").hasArg()
				.withDescription("Password to connect with")
				.withArgName("password").create("P"));

		opts.addOption(OptionBuilder.withLongOpt("debug").hasArg(false)
				.create('d'));

		opts.addOption(OptionBuilder.withLongOpt("verbose").hasArg(false)
				.create('v'));

		File configurationFile = new File("config.xml");

		try {
			// Parse the command-line options
			CommandLine cmds = parser.parse(opts, args);

			if (!cmds.hasOption('p')) {
				Logger.getRootLogger().addAppender(
						new GuiErrorAlerter(Level.ERROR));
			}

			Logger.getRootLogger().setLevel(Level.ERROR);
			if (cmds.hasOption('v')) {
				Logger.getRootLogger().setLevel(Level.INFO);
			}
			if (cmds.hasOption('d')) {
				Logger.getRootLogger().setLevel(Level.DEBUG);
			}

			log.debug("Enabling configuration file parsing");
			// The user has given us a file to parse.
			if (cmds.hasOption("c")) {
				configurationFile = new File(cmds.getOptionValue("c"));
			}
			log.debug("Config file: " + configurationFile);

			// Load the configuration file
			if (configurationFile.exists()) {
				config.load(configurationFile);
			} else {
				log.error("Cannot find config file");
			}

			/*
			 * Convert the profiles into memory
			 */
			Vector<ConnectionProfile> profs = new Vector<ConnectionProfile>();
			List<?> profList = config.configurationAt("Profiles")
					.configurationsAt("Profile");
			for (Object p : profList) {
				SubnodeConfiguration profile = (SubnodeConfiguration) p;
				String name = profile.getString("[@name]");
				String auth = profile.getString("LdapAuthString");
				String server = profile.getString("LdapServerString");
				String group = profile.getString("LdapGroupsLocation");
				ConnectionProfile prof = new ConnectionProfile(name, server,
						auth, group);
				profs.add(prof);
			}
			ConnectionProfile prof = null;
			if (!cmds.hasOption('p')) {
				/*
				 * Deploy the profile selector, to select a profile
				 */
				ProfileSelector profSel = new ProfileSelector(profs);
				prof = profSel.getSelection();
				if (prof == null) {
					return;
				}
				/*
				 * Empty the profiles and load a clean copy - then save it back
				 * to the file
				 */
				config.clearTree("Profiles");
				for (ConnectionProfile p : profSel.getProfiles()) {
					config.addProperty("Profiles.Profile(-1)[@name]",
							p.getName());
					config.addProperty("Profiles.Profile.LdapAuthString",
							p.getLdapAuthString());
					config.addProperty("Profiles.Profile.LdapServerString",
							p.getLdapServerString());
					config.addProperty("Profiles.Profile.LdapGroupsLocation",
							p.getLdapGroupsString());
				}
				config.save(configurationFile);
			} else {
				for (ConnectionProfile p : profs) {
					if (p.getName().equals(cmds.getOptionValue('p'))) {
						prof = p;
						break;
					}
				}
			}

			log.info("User selected " + prof);

			String password = "";
			if (cmds.hasOption('P')) {
				password = cmds.getOptionValue('P');
			} else {
				password = PasswordPrompter.promptForPassword("Password?");
			}

			if (password.equals("")) {
				return;
			}

			LdapInterface ldap = new LdapInterface(prof.getLdapServerString(),
					prof.getLdapAuthString(), prof.getLdapGroupsString(),
					password);

			/*
			 * Gather options information from the configuration engine for the
			 * specified report.
			 */
			Hashtable<String, Hashtable<String, ReportOption>> reportDataStructure = new Hashtable<String, Hashtable<String, ReportOption>>();
			List<?> repConfig = config.configurationAt("Reports")
					.configurationsAt("Report");
			for (Object p : repConfig) {
				SubnodeConfiguration repNode = (SubnodeConfiguration) p;

				// TODO Do something with the report profile.
				// Allowing the user to deploy "profiles" is a nice feature
				// String profile = repNode.getString("[@profile]");

				String reportName = repNode.getString("[@report]");
				Hashtable<String, ReportOption> reportOptions = new Hashtable<String, ReportOption>();
				reportDataStructure.put(reportName, reportOptions);
				// Loop through the options and add each to the table.
				for (Object o : repNode.configurationsAt("option")) {
					SubnodeConfiguration option = (SubnodeConfiguration) o;
					String name = option.getString("[@name]");
					String type = option.getString("[@type]");
					String value = option.getString("[@value]");

					ReportOption ro = new ReportOption();
					ro.setName(name);

					if (type.toLowerCase().equals("boolean")) {
						ro.setBoolValue(Boolean.parseBoolean(value));
					} else if (type.toLowerCase().equals("integer")) {
						ro.setIntValue(Integer.valueOf(value));
					} else {
						// Assume a string type here then.
						ro.setStrValue(value);
					}
					reportOptions.put(name, ro);
					log.debug(ro);
				}
			}
			System.out.println(reportDataStructure);

			/*
			 * At this point, we now need to deploy the reports window to have
			 * the user pick a report and select some happy options to allow
			 * that report to work. Let's go.
			 */
			/*
			 * Deploy the Reports selector, to select a report
			 */
			Reports.getInstance().setLdapConnection(ldap);
			ReportsWindow reports = new ReportsWindow(Reports.getInstance()
					.getAllReports(), reportDataStructure);
			Report report = reports.getSelection();
			if (report == null) {
				return;
			}
			/*
			 * Empty the profiles and load a clean copy - then save it back
			 * to the file
			 */
			config.clearTree("Reports");
			for (Report r : Reports.getInstance().getAllReports()) {
				config.addProperty("Reports.Report(-1)[@report]", r.getClass().getCanonicalName());
				config.addProperty("Reports.Report[@name]", "Standard");
				for(ReportOption ro: r.getOptions().values()){
					config.addProperty("Reports.Report.option(-1)[@name]", ro.getName());
					config.addProperty("Reports.Report.option[@type]", ro.getType());
					config.addProperty("Reports.Report.option[@value]", ro.getStrValue());
				}
			}
			config.save(configurationFile);
			
			report.execute();

		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("FAST Ldap Searcher", opts);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			log.fatal("OH EM GEES!  Configuration errors.");
		}

	}

}
