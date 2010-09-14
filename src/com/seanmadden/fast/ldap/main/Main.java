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
import java.util.List;
import java.util.Vector;

import org.apache.commons.cli.*;
import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import com.seanmadden.fast.ldap.ConnectionProfile;
import com.seanmadden.fast.ldap.gui.PasswordPrompter;
import com.seanmadden.fast.ldap.gui.ProfileSelector;

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

		File configurationFile = new File("config.xml");

		try {
			// Parse the command-line options
			CommandLine cmds = parser.parse(opts, args);

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

			/*
			 * Deploy the profile selector, to select a profile
			 */
			ProfileSelector profSel = new ProfileSelector(profs);
			ConnectionProfile prof = profSel.getSelection();
			if (prof == null) {
				return;
			}
			log.info("User selected " + prof);

			/*
			 * Empty the profiles and load a clean copy - then save it back to
			 * the file
			 */
			config.clearTree("Profiles");
			for (ConnectionProfile p : profSel.getProfiles()) {
				config.addProperty("Profiles.Profile(-1)[@name]", p.getName());
				config.addProperty("Profiles.Profile.LdapAuthString",
						p.getLdapAuthString());
				config.addProperty("Profiles.Profile.LdapServerString",
						p.getLdapServerString());
				config.addProperty("Profiles.Profile.LdapGroupsLocation",
						p.getLdapGroupsString());
			}
			config.save(configurationFile);
			
			String password = PasswordPrompter.promptForPassword("Password?");
			System.out.println(password);
			
			
		} catch (ParseException e) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("FAST Ldap Searcher", opts);
		} catch (ConfigurationException e) {
			e.printStackTrace();
			log.fatal("OH EM GEES!  Configuration errors.");
		}

	}

}
