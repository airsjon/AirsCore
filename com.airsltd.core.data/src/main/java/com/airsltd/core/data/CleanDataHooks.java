/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clean version of IComplexDataHooks.
 * <p>
 * Certain data tables will force a need for DataHooks without actually needing
 * any.
 *
 * @author Jon Boley
 *
 */
public class CleanDataHooks implements IComplexDataHooks {

	@Override
	public void startEndBlock(Connection p_connection) throws SQLException {
		// this clean variation needs to do nothing
	}

	@Override
	public void startBlock() {
		// this clean variation needs to do nothing
	}

	@Override
	public void finishEndBlock(Connection p_connection) throws SQLException {
		// this clean variation needs to do nothing
	}

	@Override
	public void cancelBlock() {
		// this clean variation needs to do nothing
	}

}
