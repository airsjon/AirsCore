/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.Callable;

import com.airsltd.core.NotificationStatus;

/**
 * @author Jon Boley
 *
 */
public class RemoveImmediateCall<T extends IBlockData> implements Callable<Boolean> {

	private final T f_data;
	private final BlockProvider<T> f_provider;

	public RemoveImmediateCall(T p_data, BlockProvider<T> p_provider) {
		super();
		f_data = p_data;
		f_provider = p_provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Boolean call() throws Exception {
		boolean l_retVal = false;
		try (Connection ac = CoreInterface.getSystem().getConnection(); Statement l_statement = ac.createStatement()) {
			final String l_sqlStr = "DELETE FROM `" + f_data.tableName() + "` WHERE " + f_data.selectClause("", true);
			f_provider.trace("Removing sql: " + l_sqlStr);
			l_retVal = l_statement.executeUpdate(l_sqlStr) != 0;
		} catch (final SQLException e) {
			CoreInterface.getSystem().handleException("Remove failed", e, NotificationStatus.BLOCK);
		}
		if (l_retVal) {
			for (final IModelListener<T> l_model : f_provider.getModelListeners()) {
				l_model.removeData(new HashSet<T>(Arrays.asList(f_data)));
			}
		}
		return l_retVal;
	}

}
