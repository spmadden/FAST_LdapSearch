/*
 * ReportOption.java
 * 
 *  $Id$
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

/**
 * This class represents a single option for a report.
 * 
 * @author Sean P Madden
 */
public class ReportOption {

	public enum Type {
		String, Integer, Boolean
	};

	private String name = "";
	private String description = "";
	private String help = "";

	private Type type = Type.String;
	private String strValue = "";
	private Integer intValue = 0;
	private Boolean boolValue = true;

	public ReportOption(){
		
	}
	
	public ReportOption(String name, String description, String help,
			String strValue) {
		this.name = name;
		this.description = description;
		this.help = help;
		this.strValue = strValue;
		this.type = Type.String;
	}

	public ReportOption(String name, String description, String help,
			Integer intValue) {
		this.name = name;
		this.description = description;
		this.help = help;
		this.intValue = intValue;
		this.type = Type.Integer;
	}

	public ReportOption(String name, String description, String help,
			Boolean boolValue) {
		this.name = name;
		this.description = description;
		this.help = help;
		this.boolValue = boolValue;
		this.type = Type.Boolean;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#hashCode()
	 * @return
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boolValue == null) ? 0 : boolValue.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((help == null) ? 0 : help.hashCode());
		result = prime * result
				+ ((intValue == null) ? 0 : intValue.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((strValue == null) ? 0 : strValue.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ReportOption)) {
			return false;
		}
		ReportOption other = (ReportOption) obj;
		if (boolValue == null) {
			if (other.boolValue != null) {
				return false;
			}
		} else if (!boolValue.equals(other.boolValue)) {
			return false;
		}
		if (description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!description.equals(other.description)) {
			return false;
		}
		if (help == null) {
			if (other.help != null) {
				return false;
			}
		} else if (!help.equals(other.help)) {
			return false;
		}
		if (intValue == null) {
			if (other.intValue != null) {
				return false;
			}
		} else if (!intValue.equals(other.intValue)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (strValue == null) {
			if (other.strValue != null) {
				return false;
			}
		} else if (!strValue.equals(other.strValue)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}

	/**
	 * [Place method description here]
	 *
	 * @see java.lang.Object#toString()
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportOption [");
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		if (help != null) {
			builder.append("help=");
			builder.append(help);
			builder.append(", ");
		}
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (strValue != null) {
			builder.append("strValue=");
			builder.append(strValue);
			builder.append(", ");
		}
		if (intValue != null) {
			builder.append("intValue=");
			builder.append(intValue);
			builder.append(", ");
		}
		if (boolValue != null) {
			builder.append("boolValue=");
			builder.append(boolValue);
		}
		builder.append("]");
		return builder.toString();
	}

	/**
	 * Returns the name
	 *
	 * @return name the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 *
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description
	 *
	 * @return description the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description
	 *
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the help
	 *
	 * @return help the help
	 */
	public String getHelp() {
		return help;
	}

	/**
	 * Sets the help
	 *
	 * @param help the help to set
	 */
	public void setHelp(String help) {
		this.help = help;
	}

	/**
	 * Returns the type
	 *
	 * @return type the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Sets the type
	 *
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Returns the strValue
	 *
	 * @return strValue the strValue
	 */
	public String getStrValue() {
		return strValue;
	}

	/**
	 * Sets the strValue
	 *
	 * @param strValue the strValue to set
	 */
	public void setStrValue(String strValue) {
		this.type = Type.String;
		this.strValue = strValue;
	}

	/**
	 * Returns the intValue
	 *
	 * @return intValue the intValue
	 */
	public Integer getIntValue() {
		return intValue;
	}

	/**
	 * Sets the intValue
	 *
	 * @param intValue the intValue to set
	 */
	public void setIntValue(Integer intValue) {
		this.type = Type.Integer;
		this.intValue = intValue;
	}

	/**
	 * Returns the boolValue
	 *
	 * @return boolValue the boolValue
	 */
	public Boolean getBoolValue() {
		return boolValue;
	}

	/**
	 * Sets the boolValue
	 *
	 * @param boolValue the boolValue to set
	 */
	public void setBoolValue(Boolean boolValue) {
		this.type = Type.Boolean;
		this.boolValue = boolValue;
	}
	
}
