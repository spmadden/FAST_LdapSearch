package com.seanmadden.fast.ldap;

import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
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

	public void getGroups(){
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, this.server);
		env.put(Context.SECURITY_PRINCIPAL, this.auth);
		env.put(Context.SECURITY_CREDENTIALS, this.password);
		
		try {
			DirContext ctx = new InitialDirContext(env);
			LdapCtx user = (LdapCtx)ctx.lookup(this.auth);
			LdapCtx schema = (LdapCtx)ctx.lookup("CN=Person,CN=Schema,CN=Configuration,DC=finance,DC=rit,DC=edu");
			
//			DirContext schema= user.getSchema("");
			System.out.println(schema.getSchema(""));
			NamingEnumeration<NameClassPair> schemaList = schema.getSchema("").list("");
			while(schemaList.hasMore()){
				NameClassPair ncp = schemaList.next();
				System.out.println(ncp.getName());
				System.out.println(ncp);
			}
//			Attributes userAtt = user.getAttributes("");
//			NamingEnumeration<? extends Attribute> userAtts = userAtt.getAll();
//			while(userAtts.hasMore()){
//				Attribute next = userAtts.next();
//				System.out.println(next);
//			}
//			LdapCtx e = (LdapCtx)ctx.lookup(this.groups);
//			NamingEnumeration<NameClassPair> elems = e.list("");
//			while(elems.hasMore()){
//				NameClassPair ncp = elems.next();
//				System.out.println("Name: " + ncp.getName());
//				if(ncp.getName().startsWith("OU")){
//					NamingEnumeration<NameClassPair> el = e.list(ncp.getName());
//					while(el.hasMore()){
//						NameClassPair ncp1 = el.next();
//						System.out.println(" -> " + ncp1.getName());
//					}
//				}else if(ncp.getName().startsWith("CN")){
//					System.out.println(e.getAttributes(ncp.getName()));
//				}
//				System.out.println();
//				
//			}
//			
		} catch (NamingException e) {
			if(e.getRootCause() instanceof UnknownHostException){
				log.fatal("Cannot connect to server.");
			}else if(e instanceof AuthenticationException){
				log.fatal("Unable to authenticate with given credentials");
			}else{
				e.printStackTrace();
			}
		}
	}
}
