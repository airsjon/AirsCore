/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

/**
 * Two objects are equal if they are the same type and would be persisted in the
 * same location in the database.
 *
 * @author Jon Boley
 *
 */
public interface IKeyedData {

	/**
	 * return the fields that determine if two records are the same
	 *
	 * @return
	 */
	long primaryKeyFields();

}
