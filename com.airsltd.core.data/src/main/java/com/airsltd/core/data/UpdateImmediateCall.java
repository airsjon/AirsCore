/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import com.airsltd.core.NotificationStatus;

/**
 * @author jon_000
 *
 */
public class UpdateImmediateCall<T extends IBlockData> implements Callable<Boolean> {

	private final T f_oldData;
	private final T f_newData;
	private final BlockProvider<T> f_provider;

	public UpdateImmediateCall(T p_oldData, T p_newData, BlockProvider<T> p_blockProvider) {
		f_oldData = p_oldData;
		f_newData = p_newData;
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
		try (Connection ac = CoreInterface.getSystem().getConnection()) {
			f_provider.modifyRecordHook(f_oldData, f_newData);
			l_retVal = f_provider.updateContent(ac, f_oldData, f_newData);
		} catch (final SQLException e) {
			CoreInterface.getSystem().handleException("Unable to update content: " + f_oldData + " with " + f_newData,
					e, NotificationStatus.BLOCK);
		}
		if (l_retVal) {
			final List<IUserInterfaceListener<T>> l_uiInterfaces = new ArrayList<>();
			final Set<String> l_properties = new HashSet<>();
			for (final IModelListener<T> l_model : f_provider.getModelListeners()) {
				l_properties.addAll(l_model.prepareUpdateData(new HashSet<BlockMod<T>>(Arrays.asList(new BlockMod<T>(f_oldData, f_newData))),
						l_uiInterfaces));
			}
			f_oldData.modified(f_newData);
			for (final IUserInterfaceListener<T> l_listener : l_uiInterfaces) {
				l_listener.modifyData(Arrays.asList(f_oldData), l_properties);
			}
		}
		return l_retVal;
	}

}
