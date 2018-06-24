/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.Callable;

import com.airsltd.core.NotificationStatus;

/**
 * @author jon_000
 *
 */
public class AddImmediateCall<T extends IBlockData> implements Callable<Boolean> {

	private final T f_data;
	private final BlockProvider<T> f_provider;

	public AddImmediateCall(T p_data, BlockProvider<T> p_provider) {
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
		try (Connection l_ac = CoreInterface.getSystem().getConnection()) {
			f_provider.addRecordHook(f_data);
			l_retVal = f_provider.addContent(l_ac, f_data);
			if (l_retVal) {
				for (final IModelListener<T> l_model : f_provider.getModelListeners()) {
					l_model.addData(new HashSet<T>(Arrays.asList(f_data)));
				}
			}
		} catch (final SQLException e) {
			CoreInterface.getSystem().handleException("Unable to add record: " + f_data, e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

}
