/**
 *
 */
package com.airsltd.core.data;

/**
 * @author Jon Boley
 *
 */
public interface IBlockDataExt<T extends IBlockData> {

	/**
	 * This method allows the data to inform any data blocks contained in the
	 * record that they should be added/loaded.
	 */
	void addRecord();

	/**
	 * Make sure that any changes in data blocks inside this data are
	 * added/loaded as needed.
	 *
	 * @param p_oldData
	 */
	void modifyRecord(T p_oldData);

	/**
	 * In the event that there is a one-to-one mapping of data records contained
	 * in this data, this method can be used to removed the contained data.
	 */
	void removeRecord();
}
