/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

/**
 * Objects that implement this interface provide a nice way of printing them for
 * user consumption
 *
 * @author Jon
 *
 */
public interface IPrettyObject {

	/**
	 * Return a pretty string to display the object to a user.
	 *
	 * @return the String to be presented to the user
	 *
	 */
	String niceString();

}
