/**
 *
 */
package com.airsltd.core.data;

import java.text.ParseException;

/**
 * A data structure IBlockData is made up of IBlockField items.
 *
 * @author Jon
 *
 */
public interface IBlockField<U> extends Comparable<Object> {

	/**
	 * Return a String representation of the Data of type U. This String
	 * representation should be useable in an SQL database.
	 *
	 * @return {@link String}
	 */
	String toSqlValue();

	/**
	 * Get the value for this particular field.
	 *
	 * @return U
	 */
	U getValue();

	/**
	 * Set the value for this field to p_newData
	 *
	 * @param p_newData
	 */
	void setValue(U p_newData);

	/**
	 * Set the value for this field from a {@link String} representation.
	 *
	 * @param string
	 * @throws ParseException
	 */
	void fromSqlValue(String string) throws ParseException;

}
