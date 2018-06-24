/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A RefreshBlockProvider allows for data to be stored locally and merges
 * changes onto the server.
 * <p>
 *
 * @author Jon Boley
 *
 */
public abstract class RefreshBlockProvider<T extends IBlockData> extends BlockProvider<T> {

	public RefreshBlockProvider(Class<T> p_class) {
		super(p_class);
	}

	/**
	 * When data is checked, the data may need to be added, removed or updated.
	 * The enum DataCheck is used to store that state.
	 *
	 * @author Jon Boley
	 *
	 */
	public enum DataCheck {
		ADD, REMOVE, UPDATE
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.BlockProvider#addContent(com.airsltd.core.data.
	 * IBlockData)
	 */
	@Override
	public boolean addContent(T p_data) {
		boolean rVal = true;
		if (isBlockOn()) {
			rVal = super.addContent(p_data);
		} else {
			try {
				startBlock();
				addContent(p_data);
				rVal = endBlock();
			} finally {
				cancelBlock();
			}
		}
		return rVal;
	}

	/**
	 * Determine the state of the current data.
	 * <p>
	 * After reloading a database, the system attempts to determine if the data
	 * still needs to be updated.
	 *
	 * @return a list of all the data that is going to be updated.
	 */
	public List<T> checkDataUptodate() {
		final List<T> p_dataVector = new ArrayList<T>(
				f_blockRemoveData.size() + f_blockUpdateData.size() + f_blockData.size());
		final List<T> p_dataNewVector = new ArrayList<T>(
				f_blockRemoveData.size() + f_blockUpdateData.size() + f_blockData.size());
		final List<DataCheck> p_check = new ArrayList<DataCheck>();
		loadDataChecks(p_dataVector, p_dataNewVector, p_check);
		final boolean[] l_uptoDate = isUptoDate(p_dataVector);
		final List<T> l_dataToUpdate = new ArrayList<T>();
		int l_index = 0;
		for (final boolean l_mod : l_uptoDate) {
			if (!l_mod) {
				l_dataToUpdate.add(p_dataVector.get(l_index));
			}
			l_index++;
		}
		if (!l_dataToUpdate.isEmpty()) {
			refreshDataBlock(l_dataToUpdate);
			refreshData(l_dataToUpdate);
			refreshCheckData(l_uptoDate, p_dataVector, p_dataNewVector, p_check);
			refreshDataBlockEnd(l_dataToUpdate);
		}
		return p_dataVector;
	}

	private void refreshCheckData(boolean[] p_uptoDate, List<T> p_dataVector, List<T> p_dataNewVector,
			List<DataCheck> p_check) {
		int i_index = 0;
		for (final boolean i_bool : p_uptoDate) {
			if (!i_bool) {
				switch (refreshData(p_dataVector.get(i_index), p_dataNewVector.get(i_index), p_check.get(i_index))) {
				case CONFLICT:
					/*
					 * create issue
					 */
					createConflict(p_dataVector.get(i_index), p_dataNewVector.get(i_index), p_check.get(i_index));
				case REDUNDENT:
					removeRefresh(p_dataVector.get(i_index), p_check.get(i_index));
					break;
				default:
				}
			}
			i_index++;
		}
	}

	/**
	 * Update the data from the server.
	 * <p>
	 * Data needs to be reloaded. The p_uptoDate may provide information on what
	 * needs to be updated.
	 * 
	 * @param p_dataToUpdate
	 */
	protected abstract void refreshData(List<T> p_dataToUpdate);

	/**
	 * Inform the system that a conflict exits.
	 * <p>
	 *
	 * @param p_data
	 *            not null, the data that is under conflict
	 * @param p_newData
	 *            for updates the value that the data will be set to
	 * @param p_dataCheck
	 *            not null, the method that the data was going to be updated
	 */
	protected abstract void createConflict(T p_data, T p_newData, DataCheck p_dataCheck);

	/**
	 * Remove an update because it has already been done.
	 * <p>
	 * After reloading the database, a possible change is found not to be
	 * needed.
	 * 
	 * @param p_data
	 *            not null, the data that has already been updated
	 * @param p_check
	 *            not null, the method the data was being updated
	 */
	protected void removeRefresh(T p_data, DataCheck p_check) {
		switch (p_check) {
		case REMOVE:
			f_blockRemoveData.remove(p_data);
			break;
		case UPDATE:
			removeRefreshUpdate(p_data);
			break;
		case ADD:
		default:
			f_blockData.remove(p_data);
			break;
		}
	}

	private void removeRefreshUpdate(T p_data) {
		for (final BlockMod<T> i_data : f_blockUpdateData) {
			if (i_data.getOldValue() == p_data) {
				f_blockUpdateData.remove(i_data);
				break;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.BlockProvider#endBlock()
	 */
	@Override
	public boolean endBlock() {
		final List<T> p_dataVector = checkDataUptodate();
		final boolean rVal = super.endBlock();
		makeUptoDate(p_dataVector);
		return rVal;
	}

	/**
	 * This will return the database store point associated with the current
	 * data loaded into the model.
	 *
	 * @param p_data
	 * @return
	 */
	protected abstract long getCurrentStorePoint(T p_data);

	/**
	 * Get the current store point on the Server.
	 *
	 * @return
	 */
	protected abstract long getStorePoint(T p_data);

	/**
	 *
	 * Has the data on the server side changed from what the client has locally
	 * for the list of T.
	 * <p>
	 * Data may be granualized for updates, but current (and projected)
	 * implementations do not make use of this feature.
	 * 
	 * @param p_data
	 *            not null, a {@link List} of all the data that is ready to be
	 *            updated in the database
	 *
	 * @return true if the data has not been changed.
	 *
	 */
	public boolean[] isUptoDate(List<T> p_data) {
		final boolean[] rVal = new boolean[p_data.size()];
		int i_index = 0;
		for (final T i_data : p_data) {
			rVal[i_index++] = isUptoDate(i_data);
		}
		return rVal;
	}

	/**
	 * Has the data on the server side changed from what the client has locally.
	 *
	 * @return true if the data has not been changed.
	 */
	public final boolean isUptoDate(T p_data) {
		return getStorePoint(p_data) == getCurrentStorePoint(p_data);
	}

	/**
	 * Load the two lists with the data that needs to be modified in the
	 * database.
	 * <p>
	 * p_dataVector will contain all the data that is being added, removed, or
	 * changed. p_dataNewVector will contain the new data that is being update.
	 *
	 * @param p_dataVector
	 *            not null, this list is modified and will contain all data that
	 *            is going to be updated (added, removed, changed)
	 * @param p_dataNewVector
	 *            not null, this list is modified and will contain all the data
	 *            that is being changed
	 * @param p_check
	 *            not null, this list will tell the refresh system how the data
	 *            in p_dataVector is being updated.
	 */
	protected void loadDataChecks(List<T> p_dataVector, List<T> p_dataNewVector, List<DataCheck> p_check) {
		for (final T i_data : f_blockData) {
			p_dataVector.add(i_data);
			p_dataNewVector.add(null);
			p_check.add(DataCheck.ADD);
		}
		for (final T i_removeData : f_blockRemoveData) {
			p_dataVector.add(i_removeData);
			p_dataNewVector.add(null);
			p_check.add(DataCheck.REMOVE);
		}
		for (final BlockMod<T> i_updateData : f_blockUpdateData) {
			p_dataVector.add(i_updateData.getOldValue());
			p_dataNewVector.add(i_updateData.getNewValue());
			p_check.add(DataCheck.UPDATE);
		}
	}

	/**
	 * Update the store point for all the data in <code>p_data</code>
	 * 
	 * @param p_data
	 */
	public void makeUptoDate(List<T> p_data) {
		for (final T i_data : p_data) {
			makeUptoDate(i_data);
		}
	}

	/**
	 * Sets the model to the latest store point loaded from the database. So in
	 * the future when a refresh point is checked we know that the data was
	 * updated to this point.
	 * 
	 * @param p_data
	 * @see {@link BlockProvider#makeUptoDate(List)}
	 * @category Refresh Data component
	 */
	public final void makeUptoDate(T p_data) {
		updateCurrentStorePoint(p_data);
	}

	/**
	 * Check the data against the refreshed data.
	 * <p>
	 * If this data modification is already loaded on the server, then return
	 * {@link DataState#REDUNDENT}. If the data is in conflict with the data
	 * already on the server, then return {@link DataState#CONFLICT}. If the
	 * data needs to be applied to the data already on the server, then return
	 * {@link DataState#VALID}. Use the hooks
	 * {@link BlockProvider#refreshDataBlock()} and
	 * {@link BlockProvider#refreshDataBlockEnd()} to setup and tear down any
	 * objects you need to track refresh data.
	 *
	 * @param p_data
	 *            not null, the data that is going to be added, removed or
	 *            updated on the server
	 * @param p_oldData
	 *            the new value of the data when a {@link DataCheck#UPDATE} is
	 *            being done
	 * @param p_state
	 *            not null, the type of data modification being done.
	 * @return a {@link DataState} that tells the engine how to proceed with the
	 *         data.
	 */
	protected DataState refreshData(T p_data, T p_newData, DataCheck p_state) {
		final T p_serverData = findData(p_data);
		DataState l_retVal = DataState.VALID;
		switch (p_state) {
		case ADD:
			l_retVal = refreshDataAdd(p_serverData, p_data);
			break;
		case REMOVE:
			l_retVal = refreshDataRemove(p_serverData, p_data);
			break;
		case UPDATE:
		default:
			l_retVal = refreshDataUpdate(p_serverData, p_data, p_newData);
		}
		return l_retVal;
	}

	protected DataState refreshDataUpdate(T p_serverData, T p_data, T p_newData) {
		return p_serverData == null ? DataState.CONFLICT
				: p_serverData.exactMatch(p_data) ? DataState.VALID
						: p_serverData.exactMatch(p_newData) ? DataState.REDUNDENT : DataState.CONFLICT;
	}

	protected DataState refreshDataRemove(T p_serverData, T p_data) {
		return p_serverData == null ? DataState.REDUNDENT
				: p_serverData.exactMatch(p_data) ? DataState.VALID : DataState.CONFLICT;
	}

	protected DataState refreshDataAdd(T p_serverData, T p_data) {
		return p_serverData != null ? p_serverData.exactMatch(p_data) ? DataState.REDUNDENT : DataState.CONFLICT
				: DataState.VALID;
	}

	/**
	 * Find the current data on the server.
	 *
	 * @param p_data
	 * @return the value in the current database (after a reload) that is
	 *         {@link AbstractBlockData#equals(Object)}
	 */
	protected abstract T findData(T p_data);

	/**
	 * This hook is called before a collection of data is going to be added,
	 * deleted and/or modified. It allows an implementor to create data
	 * structures necessary for tracking the refresh state during the data
	 * processing
	 * 
	 * @param p_dataToUpdate
	 */
	protected void refreshDataBlock(List<T> p_dataToUpdate) {
	}

	/**
	 * This hook is called after a collect of data is going to be added, deleted
	 * and/or modified. It allows an implementor to remove any data structures
	 * and do any clean up following a refresh of the data
	 * 
	 * @param p_dataToUpdate
	 */
	protected void refreshDataBlockEnd(List<T> p_dataToUpdate) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.BlockProvider#removeContent(com.airsltd.core.data.
	 * IBlockData)
	 */
	@Override
	public boolean removeContent(T p_data) {
		boolean rVal = true;
		if (isBlockOn()) {
			rVal = super.removeContent(p_data);
		} else {
			try {
				startBlock();
				removeContent(p_data);
				rVal = endBlock();
			} finally {
				cancelBlock();
			}
		}
		return rVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.BlockProvider#updateContent(com.airsltd.core.data.
	 * IBlockData, com.airsltd.core.data.IBlockData)
	 */
	@Override
	public boolean updateContent(T p_oldData, T p_newData) {
		boolean rVal = true;
		if (isBlockOn()) {
			rVal = super.updateContent(p_oldData, p_newData);
		} else {
			try {
				startBlock();
				updateContent(p_oldData, p_newData);
				rVal = endBlock();
			} finally {
				cancelBlock();
			}
		}
		return rVal;
	}

	/**
	 * Update the local store point for p_data.
	 *
	 * @param p_data
	 */
	protected abstract void updateCurrentStorePoint(T p_data);

}
