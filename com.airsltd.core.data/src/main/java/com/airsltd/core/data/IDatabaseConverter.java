/**
 *
 */
package com.airsltd.core.data;

import java.text.ParseException;

/**
 * Interface that declares the methods necessary for the proper conversion of a
 * Java data object to it's store representation.
 *
 * @param U is the data where the information is stored
 * @param T is the data being stored
 * 
 * @author Jon Boley
 *
 */
public interface IDatabaseConverter<U extends IBlockData, T> {
	/**
	 * Convert the data into an string that represents the data in the data store.
	 * <p>
	 * Complex methods of storing data will require information about the object where the data resides.
	 *
	 * @param p_parent  not null, the object where the data exists
	 * @param p_data  not null, the data to be converted to store
	 * @return
	 */
	String toSql(U p_parent, T p_data);

	/**
	 * Parse an data store string into the data type that this converter is implementing
	 *
	 * @param p_parent  not null, the parent where the data will exist
	 * @param p_string  not null, the string representation of the data in the store
	 * @return a data of type T
	 */
	T fromSql(U p_parent, String p_string) throws ParseException;
}
