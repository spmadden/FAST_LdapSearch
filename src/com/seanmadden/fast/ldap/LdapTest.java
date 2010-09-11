/*
* LdapTest.java
* 
* 	$Id$
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
package com.seanmadden.fast.ldap;

import javax.naming.*;
import javax.naming.directory.*;

import com.sun.jndi.ldap.LdapCtx;

import java.util.Hashtable;

/**
 * [Insert class description here]
 *
 * @author Sean P Madden
 */
public class LdapTest {

	/**
	 * [Place method description here]
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://finance.rit.edu");
		env.put(Context.SECURITY_PRINCIPAL, "cn=spmfa,OU=Support,OU=Systems & Technology,OU=Finance & Administration,DC=finance,DC=rit,DC=edu");
		env.put(Context.SECURITY_CREDENTIALS, "");
		
		try {
			DirContext ctx = new InitialDirContext(env);
			LdapCtx e = (LdapCtx)ctx.lookup("OU=Groups,DC=finance,DC=rit,DC=edu");
			NamingEnumeration<NameClassPair> elems = e.list("");
			while(elems.hasMore()){
				System.out.println(elems.next());
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		
	}

}
