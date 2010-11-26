/*
 * ReportFinder.java
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
package com.seanmadden.spikes;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.*;

import com.seanmadden.fast.ldap.reports.Report;

/**
 * This class will iterate through the classpath and find all instances of
 * classes that implement 'Report'
 * 
 * @author Sean P Madden
 */
public class ClassFinder {

	/**
	 * Default constructor
	 * 
	 */
	private ClassFinder() {

	}

	/**
	 * Default entry point, accepts a Class file as a search term
	 * 
	 * @param cls
	 * @return
	 */
	public static Vector<String> findAllInterfaces(Class<?> cls) {
		Vector<String> vect = new Vector<String>();
		String classpath = System.getProperty("java.class.path");
		String[] paths = classpath.split(System.getProperty("path.separator"));
		for (String path : paths) {
			// This bullshit is to deal with jars on the classpath.
			if(path.endsWith(".jar")){
				try {
					JarFile file = new JarFile(path);
					Enumeration<JarEntry> entries = file.entries();
					while(entries.hasMoreElements()){
						String className = entries.nextElement().toString().replace("/", ".");
						if(!className.endsWith(".class") || className.contains("$") || !className.startsWith("com")){
							continue;
						}
						className = className.substring(0, className.length() - 6);
						try {
							Class<?> c = Class.forName(className);
							if(c.getSuperclass() != null && c.getSuperclass().getName().equals(cls.getName())){
								vect.add(c.getCanonicalName());
								continue;
							}
							for (Class<?> cl : c.getInterfaces()) {
								if (cl.getName().equals(cls.getName())) {
									vect.add(className);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else{
				vect.addAll(findClassFiles(path, path, cls));
			}
		}
		return vect;
	}

	/**
	 * Private recursive loop to find class files.
	 * 
	 * @param path
	 * @param startPath
	 * @param toMatch
	 * @return
	 */
	private static Vector<String> findClassFiles(String path, String startPath,
			Class<?> toMatch) {
		String sep = System.getProperty("file.separator");
		Vector<String> paths = new Vector<String>();
		File f = new File(path);
		if (f.isDirectory()) {
			for (File fs : f.listFiles()) {
				paths.addAll(findClassFiles(path + sep + fs.getName(),
						startPath, toMatch));
			}
			return paths;
		} else {
			if (!f.getAbsolutePath().endsWith(".class")) {
				return paths;
			}
			try {
				String cls = f.getCanonicalPath().replaceAll(sep + sep, ".")
						.substring(startPath.length() + sep.length());
				cls = cls.substring(0, cls.length() - 6);
				cls = cls.replace('/', '.');
				if(!cls.startsWith("com")){
					return paths;
				}
				Class<?> c = Class.forName(cls);
				for (Class<?> cl : c.getInterfaces()) {
					if (cl.getName().equals(toMatch.getName())) {
						paths.add(cls);
					}
				}
				c = c.getSuperclass();
				if (c != null) {
					if (c.getName().equals(toMatch.getName())) {
						paths.add(cls);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		return paths;

	}

	public static void main(String[] args) {
		System.out.println(findAllInterfaces(Report.class));
	}

}
