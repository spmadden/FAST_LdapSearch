/*
 * Reports.java
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

import java.util.Collection;
import java.util.Vector;

import com.seanmadden.utilities.ClassFinder;

/**
* This class is the entrypoint for all the reports functions.  A report will register itself for it's availablility to be displayed and used.
*
* @author Sean P Madden
*/
public class Reports {

        /**
         * The reports object.
         */
        private static Reports theReport = null;
        
        /**
         * A list of all the available reports
         */
        private Vector<Report> reports = new Vector<Report>();
        
        /**
         * Ugh.. A singleton.  I dislike.
         *
         */
        private Reports() {
                Vector<String> reps = ClassFinder.findAllInterfaces(Report.class);
                for(String r : reps){
                        try {
                                reports.add((Report)Class.forName(r).newInstance());
                        } catch (InstantiationException e) {
                                e.printStackTrace();
                        } catch (IllegalAccessException e) {
                                e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                        }
                }
        }
        
        public Collection<Report> getAllReports(){
                return this.reports;
        }
        
        /**
        * IT'S A SINGLETON!
        * 
        * @return WAHHHH
        */
        public static Reports getInstance(){
                if(theReport == null) theReport = new Reports();
                return theReport;
        }
        
}
