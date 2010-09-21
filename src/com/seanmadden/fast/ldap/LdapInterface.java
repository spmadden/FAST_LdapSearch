package com.seanmadden.fast.ldap;

import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.apache.log4j.Logger;

import com.sun.jndi.ldap.LdapCtx;

public class LdapInterface {
	
	private static Logger log = Logger.getLogger(LdapInterface.class);

	private String server = "";
	private String auth = "";
	private String groups = "";
	private String password = "";
	
	public LdapInterface(String server, String auth, String groups, String password) {
		this.server = server;
		this.auth = auth;
		this.groups = groups;
		this.password = password;
		
	}

	public List<LdapGroup> getGroups(){
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, this.server);
		env.put(Context.SECURITY_PRINCIPAL, this.auth);
		env.put(Context.SECURITY_CREDENTIALS, this.password);
		
		Vector<LdapGroup> groups = new Vector<LdapGroup>();
		
		try {
			DirContext ctx = new InitialDirContext(env);
			LdapCtx e = (LdapCtx)ctx.lookup(this.groups);
			NamingEnumeration<NameClassPair> elems = e.list("");
			while(elems.hasMore()){
				NameClassPair ncp = elems.next();
//				System.out.println("Name: " + ncp.getName());
				LdapGroup group = new LdapGroup(e, ncp.getName().substring(3));
				if(ncp.getName().startsWith("OU")){
					group.setOu(true);
//					NamingEnumeration<NameClassPair> el = e.list(ncp.getName());
//					while(el.hasMore()){
//						NameClassPair ncp1 = el.next();
//						System.out.println(" -> " + ncp1.getName());
//					}
				}else if(ncp.getName().startsWith("CN")){
//					System.out.println(e.getAttributes(ncp.getName()));
				}
				groups.add(group);
//				System.out.println();
			}
			return groups;
		} catch (NamingException e) {
			if(e.getRootCause() instanceof UnknownHostException){
				log.fatal("Cannot connect to server.");
			}else if(e instanceof AuthenticationException){
				log.fatal("Unable to authenticate with given credentials");
			}else{
				e.printStackTrace();
			}
		}
		return null;
	}
}
