/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core;

/**
 * Objects that implement this interface provide a
 * <a href="www.json.org">json</a> reprsentation of themselves.
 *
 * @author Jon Boley
 *
 */
public interface JSONProvider {

	/**
	 * Convert the object to a <a href="www.json.org">json</a> representation.
	 * 
	 * @return
	 */
	String toJSON();

}
