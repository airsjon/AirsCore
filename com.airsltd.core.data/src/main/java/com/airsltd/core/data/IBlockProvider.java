/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

/**
 *
 * @author Jon
 *
 */
public interface IBlockProvider<T> extends IPersistentStore<T> {

	/**
	 * StartBlock sets the {@link IPersistentStore} into block mode. Block mode
	 * is used when a large number of records need to be updated into the
	 * system. The table should be locked and a marker to rewind created in case
	 * of a need to restore the database to its original state.
	 */
	void startBlock();

	void cancelBlock();

	/**
	 * Process all block removes, updates and adds. We do them in that order so
	 * there are no issues with duplicates, etc. As long as the blocks are
	 * properly formed :)
	 *
	 * Any locks and markers placed on the database are removed.
	 *
	 * @return if the data updates are successful true is returned, otherwise
	 *         false
	 */
	boolean endBlock();

	/**
	 * Process all block removes, updates and adds. We do them in that order so
	 * there are no issues with duplicates, etc. As long as the blocks are
	 * properly formed :)
	 *
	 * Any locks and markers placed on the database are removed.
	 *
	 * @param booleanDeleteClause
	 *            informs the method to use the {@link IBlockData}'s delete
	 *            clause for filter checks.
	 * @return if the data updates are successful true is returned, otherwise
	 *         false
	 */
	boolean endBlock(boolean p_booleanDeleteClause);

	/**
	 * @return the blockOn
	 */
	boolean isBlockOn();

	/**
	 * @param blockOn
	 *            the blockOn to set
	 */
	void setBlockOn(boolean blockOn);

	/**
	 * @return the blockData
	 */
	Set<T> getBlockData();

	/**
	 * @return the blockRemoveData
	 */
	Set<T> getBlockRemoveData();

	/**
	 * @return the blockUpdateData
	 */
	Set<BlockMod<T>> getBlockUpdateData();

	Set<T> getBlockUpdateOriginals();

	/**
	 * this hook provides a good place to assign IDs for a set of data before
	 * they are added to the database
	 */
	void assignIds(Connection conn) throws SQLException;

}
