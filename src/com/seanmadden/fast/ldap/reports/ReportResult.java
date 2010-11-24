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

import java.util.Collection;

/**
* This interface represents the result of a report.
*
* @author Sean P Madden
*/
public interface ReportResult {
        /**
        * This method will return a list of the available paramenters the report line holds.
        * 
        * @return
        */
        public Collection<String> getParemeters();
        
        /**
        * This method will return the value of a single parameter.
        * 
        * @param param
        * @return
        */
        public Object getParameter(String param);
}