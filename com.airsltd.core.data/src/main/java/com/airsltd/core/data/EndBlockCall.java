/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.airsltd.core.NotificationStatus;

/**
 * @author jon_000
 *
 */
public class EndBlockCall<T extends IBlockData> implements Callable<Boolean> {

	private final boolean f_useDeleteClause;
	private final BlockProvider<T> f_provider;

	public EndBlockCall(boolean p_useDeleteClause, BlockProvider<T> p_blockProvider) {
		f_useDeleteClause = p_useDeleteClause;
		f_provider = p_blockProvider;
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
			final boolean l_commitState = l_ac.getAutoCommit();
			/*
			 * initialize connection state
			 */
			Savepoint l_sp = f_provider.initializeConnection(l_ac);
			try {
				l_ac.setAutoCommit(false);
				l_retVal = f_provider.endBlock(l_ac, f_useDeleteClause);
				f_provider.trace("End block commit changes");
				l_ac.commit();
				l_sp = null;
			} finally {
				if (l_sp != null) {
					f_provider.trace("Attempt Rollback");
					l_ac.rollback(l_sp);
				}
				l_ac.setAutoCommit(l_commitState);
			}
		} catch (final SQLException p_exception) {
			CoreInterface.getSystem().handleException("Unable to do block updates", p_exception,
					NotificationStatus.BLOCK);
		}
		if (l_retVal) {
			final List<IUserInterfaceListener<T>> l_listeners = new ArrayList<>();
			final Set<String> l_properties = new HashSet<>();
			for (final IModelListener<T> l_model : f_provider.getModelListeners()) {
				l_model.blockUIStart();
				l_model.removeData(f_provider.getBlockRemoveData());
				l_properties.addAll(l_model.prepareUpdateData(f_provider.getBlockUpdateData(), l_listeners));
				l_model.addData(f_provider.getBlockData());
				l_model.blockUIStop();
			}
			final List<T> l_modifiedList = new ArrayList<>();
			for (final BlockMod<T> l_data : f_provider.getBlockUpdateData()) {
				final T l_oldValue = l_data.getOldValue();
				final T l_newValue = l_data.getNewValue();
				l_modifiedList.add(l_oldValue);
				l_oldValue.copy(l_newValue);
			}
			for (final IUserInterfaceListener<T> l_listener : l_listeners) {
				l_listener.modifyData(l_modifiedList, l_properties);
			}
		}
		return l_retVal;
	}

}
