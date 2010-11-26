/*
 * ASCIIFormatter.java
 * 
 * Copyright (C) 2010 Sean P Madden
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you would like to license this code under the GNU LGPL, please see
 * http://www.seanmadden.net/licensing for details.
 */
package com.seanmadden.fast.ldap.reports;

import java.io.*;
import java.rmi.AccessException;

/**
 * The ASCII Formatter accepts a ReportResult and formats it into a
 * Tab-Delimited sequence.
 * 
 * @author Sean P Madden
 */
public class ASCIIFormatter implements Formatter {

	private static String header = "=========================================================\r\n|            ___  __    /  ___       __  ___            |\r\n|          |  |  /__`  /  |__   /\\  /__`  |             |\r\n|          |  |  .__/ /   |    /~~\\ .__/  |             |\r\n|       __        __      __   ___  __   __   __  ___   |\r\n| |    |  \\  /\\  |__)    |__) |__  |__) /  \\ |__)  |    |\r\n| |___ |__/ /~~\\ |       |  \\ |___ |    \\__/ |  \\  |    |\r\n|                                                       |\r\n=========================================================\r\n";
	
	/**
	 * Make me an ASCII Formatter!
	 * 
	 */
	public ASCIIFormatter() {

	}

	public boolean format(ReportResult res, File output) throws Exception {
		if(!output.canWrite()){
			if(output.exists()){
				throw new AccessException("Cannot write to file");
			}
			output.createNewFile();
		}
		FileWriter writer = new FileWriter(output);
		writer.write(header);
		writer.write("\r\n");
		writer.write("Run At:  " + res.getRunAtDate() + "\r\n");
		writer.write("Run By:  " + res.getRunByCn() + "/" + res.getRunByDn() + "\r\n");
		writer.write("\r\n");
		writer.write(res.getReportName() + " for user \r\n     " + res.getForCn() + "/"+res.getForDn() + "\r\n");

		for(String section : res.getSections()){
			writer.write("\r\nSection: " + section + "\r\n");
			
			//Calculate the largest value of the columns
			int[] longest = new int[res.getSectionHeaders(section).size()];
			int pos = 0;
			for(String header : res.getSectionHeaders(section)){
				if(header.length() > longest[pos]){
					longest[pos] = header.length();
				}
				++pos;
			}
			for(String[] values : res.getSectionFields(section)){
				pos = 0;
				for(String value : values){
					if(value.length() > longest[pos]){
						longest[pos] = value.length();
					}
					++pos;
				}
			}
			
			// now write the columns with the proper spacing.
			pos = 0;
			for(String header : res.getSectionHeaders(section)){
				writer.write(header + numOfSpaces(longest[pos++]-header.length()) + "|");
			}
			writer.write("\r\n==============================\r\n");
			for(String[] values : res.getSectionFields(section)){
				pos = 0;
				for(String value : values){
					writer.write(value + numOfSpaces(longest[pos++]-value.length()) + "|");
				}
				writer.write("\r\n");
			}
		}
		writer.flush();
		writer.close();
		
		return true;
	}
	
	private String numOfSpaces(int spaces){
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < spaces; ++i){
			buf.append(" ");
		}
		return buf.toString();
	}

}
