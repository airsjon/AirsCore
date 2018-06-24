/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import com.airsltd.core.data.IBlockData;

/**
 * Classes that implement this interface are capable of storing their data in a
 * CSV style.
 *
 * @author Jon Boley
 *
 */
public interface ICsvData extends IBlockData {

	/**
	 * Return an array of {@link String} that could be used to store the Object
	 * in a CSV file
	 *
	 * @return
	 */
	String[] toStringCsv();

	/**
	 * Load the data from an array of strings (most likely from a CSV file)
	 *
	 * @param p_strings
	 */
	void fromStringCsv(String[] p_strings);

}
