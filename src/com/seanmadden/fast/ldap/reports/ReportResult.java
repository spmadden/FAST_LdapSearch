/*
 * ReportResult.java
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
package com.seanmadden.fast.ldap.reports;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * This interface represents the result of a report.
 * 
 * @author Sean P Madden
 */
public abstract class ReportResult {

	protected Date runDate = new Date();
	protected DateFormat dateFormatter = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	protected String runAtDate = dateFormatter.format(runDate);
	protected String forCn = "";
	protected String forName = "";
	protected String forDn = "";
	protected String runByDn = "";
	protected String runByCn = "";

	protected String reportName = "";

	/**
	 * This method will return a list of the available sections the report
	 * holds.
	 * 
	 * @return
	 */
	public abstract Collection<String> getSections();

	public abstract Collection<String> getSectionHeaders(String section);

	public abstract Collection<String[]> getSectionFields(String section);

	/**
	 * Returns the forCn
	 *
	 * @return forCn the forCn
	 */
	public String getForCn() {
		return forCn;
	}

	/**
	 * Sets the forCn
	 *
	 * @param forCn the forCn to set
	 */
	protected void setForCn(String forCn) {
		this.forCn = forCn;
	}

	/**
	 * Returns the forDn
	 *
	 * @return forDn the forDn
	 */
	public String getForDn() {
		return forDn;
	}

	/**
	 * Sets the forDn
	 *
	 * @param forDn the forDn to set
	 */
	protected void setForDn(String forDn) {
		this.forDn = forDn;
	}

	/**
	 * Returns the runByDn
	 *
	 * @return runByDn the runByDn
	 */
	public String getRunByDn() {
		return runByDn;
	}

	/**
	 * Sets the runByDn
	 *
	 * @param runByDn the runByDn to set
	 */
	protected void setRunByDn(String runByDn) {
		this.runByDn = runByDn;
	}

	/**
	 * Returns the runByCn
	 *
	 * @return runByCn the runByCn
	 */
	public String getRunByCn() {
		return runByCn;
	}

	/**
	 * Sets the runByCn
	 *
	 * @param runByCn the runByCn to set
	 */
	protected void setRunByCn(String runByCn) {
		this.runByCn = runByCn;
	}

	/**
	 * Returns the reportName
	 *
	 * @return reportName the reportName
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * Sets the reportName
	 *
	 * @param reportName the reportName to set
	 */
	protected void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * Returns the runAtDate
	 *
	 * @return runAtDate the runAtDate
	 */
	public String getRunAtDate() {
		return runAtDate;
	}

	/**
	 * Returns the forName
	 *
	 * @return forName the forName
	 */
	public String getForName() {
		return forName;
	}

	/**
	 * Sets the forName
	 *
	 * @param forName the forName to set
	 */
	protected void setForName(String forName) {
		this.forName = forName;
	}

}
