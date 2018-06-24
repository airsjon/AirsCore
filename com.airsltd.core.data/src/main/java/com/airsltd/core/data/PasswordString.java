/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

/**
 * A thin layer over String this is used to differentiate a Password string from
 * a normal string
 *
 * @author Jon
 *
 */
public class PasswordString {

	private String f_password;

	/**
	 * Create a PasswordString from {@link String} p_password
	 *
	 * @param p_password
	 */
	public PasswordString(String p_password) {
		f_password = p_password;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return f_password;
	}

	/**
	 * @param p_password
	 *            the password to set
	 */
	public void setPassword(String p_password) {
		f_password = p_password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object p_arg0) {
		return p_arg0 instanceof PasswordString ? f_password.equals(((PasswordString) p_arg0).getPassword()) : false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return f_password.hashCode();
	}

}
