/*
* Report.java
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

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.seanmadden.xmlconfiguration.XMLDataValue;

/**
* This interface represents a single report.
*
* @author Sean P Madden
*/
public abstract class Report {
	
		public static Logger logger = Logger.getLogger(Report.class);
        
        public Report(){};
        
        /**
        * Executes the specified report
        * 
        */
        public abstract void execute();
        
        /**
        * Returns a list of ReportResult objects
        * 
        * @return
        */
        public abstract Collection<ReportResult> getResult();
        
        /**
        * Returns the name of this report.
        * 
        * @return
        */
        public String getName(){
                return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.')+1);
        }
        
        public boolean hasOptions(){
        	return false;
        }
        
        public Collection<ReportOption> getOptions(){
        	return new Vector<ReportOption>();
        }
        
        public String toString(){
        	return getName();
        }
}
