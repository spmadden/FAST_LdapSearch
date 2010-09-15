package com.seanmadden.fast.ldap.gui;

import javax.swing.JOptionPane;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class GuiErrorAlerter implements Appender {

	private Filter filter = null;
	private Layout layout = null;
	private ErrorHandler errors = null;
	private String name = "";
	private Level level = Level.ERROR;
	
	public GuiErrorAlerter(Level level) {
		this.level = level;
	}

	public void addFilter(Filter arg0) {
		this.filter = (arg0);
	}

	public void clearFilters() {
		this.filter = null;
	}

	public void close() {

	}

	public void doAppend(LoggingEvent arg0) {
		if(arg0.getLevel().isGreaterOrEqual(level)){
			JOptionPane.showMessageDialog(null,"["+arg0.getLevel()+"] " +arg0.getMessage());
		}
	}

	public ErrorHandler getErrorHandler() {
		return this.errors;
	}

	public Filter getFilter() {
		return this.filter;
	}

	public Layout getLayout() {
		return this.layout;
	}

	public String getName() {
		return name;
	}

	public boolean requiresLayout() {
		return false;
	}

	public void setErrorHandler(ErrorHandler arg0) {
		this.errors = arg0;
	}

	public void setLayout(Layout arg0) {
		this.layout = arg0;
	}

	public void setName(String arg0) {
		this.name = arg0;
	}

}
