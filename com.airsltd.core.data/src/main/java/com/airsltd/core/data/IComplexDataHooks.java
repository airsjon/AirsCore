/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * When data being modeled references other data, the data model most be
 * properly set up for block processing. IComplexDataHooks allow the
 * {@link BlockProvider} to set up referenced data.
 *
 * @author Jon Boley
 *
 */
public interface IComplexDataHooks {

	/**
	 * Setup the model/providers needed for referenced data.
	 * <p>
	 * Called by the {@link BlockProvider} when a block update is started. This
	 * hook should be used to setup calls to {@link BlockProvider#startBlock()}
	 * or {@link BlockModel#startBlock()}
	 *
	 * @see BlockProvider#startBlock()
	 * @see BlockModel#startBlock()
	 */
	void startBlock();

	/**
	 * Process Block updates before ID's are assigned to the new data.
	 * <p>
	 * Called by the {@link BlockProvider} when a block update is finishing.
	 * This hook is called before the parent data has been completed. This is
	 * for data where the reference is upstream.
	 *
	 * @see BlockProvider#endBlock()
	 * @see BlockModel#endBlock()
	 *
	 * @param p_connection
	 * @throws SQLException
	 */
	void startEndBlock(Connection p_connection) throws SQLException;

	/**
	 * Process Block updates after ID's are assigned.
	 * <p>
	 * Called by the {@link BlockProvider} when the block update is finishing.
	 * This hook is called after the parent data has been setup. This is for
	 * data where the reference is down stream.
	 *
	 * @see BlockProvider#endBlock()
	 * @see BlockModel#endBlock()
	 *
	 * @param p_connection
	 *            not null, the {@link Connection} being used to access the data
	 * @throws SQLException
	 */
	void finishEndBlock(Connection p_connection) throws SQLException;

	/**
	 * Called when the block updating has been cancelled.
	 */
	void cancelBlock();

}
