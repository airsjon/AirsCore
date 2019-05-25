/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Iterator;

import com.airsltd.core.NotificationStatus;

/**
 * This base class provides the code to insert n (defaults to 40) records at a
 * time into a database
 *
 * @author Jon Boley
 * @see #validUpdate(IBlockData)
 *
 */
public class BlockProvider<T extends IBlockData> implements IBlockProvider<T> {

	public static final Object REFRESH = new Object();
	public static final Object[] EMPTYARRAY = new Object[0];
	public static final List<Object> EMPTYLIST = new ArrayList<Object>();
	private static final int DEFAULTBLOCKSIZE = 40;
	private static final String TRACEBLOCK = "/trace/block";
	private static final String DEBUGBLOCK = "/debug/block";
	private static final String INSERTPRE = "INSERT INTO `";
	private static final String ENDQUOTE = "` ";
	private static final String VALUESSTR = " VALUES ";
	private static final String BLOCKERROR = "Unable to %s record for table %s%s";
	private static final Object ADDERROR = "add";
	private static final String REMOVEERROR = "remove";
	private static final String UPDATEERROR = "update";

	/**
	 * Data to be added to the persistent store
	 */
	protected Set<T> f_blockData = new HashSet<T>();
	/**
	 * Data to be removed from the persistent store
	 */
	protected Set<T> f_blockRemoveData = new HashSet<T>();
	/**
	 * Data to be modified in the persistent store
	 */
	protected Set<BlockMod<T>> f_blockUpdateData = new HashSet<BlockMod<T>>();
	/**
	 * State that represents that the instance is in block mode
	 */
	protected boolean f_blockOn;
	/**
	 * The number of records to be processed in each block
	 */
	private int f_blockSize = DEFAULTBLOCKSIZE;
	/**
	 * Check for duplication before adding a new record. When a system knows
	 * that no duplication check is needed then set this to true to speed up
	 * processing
	 */
	private boolean f_noDuplicateCheck;
	/**
	 * An {@link IModelListener} can listen to this provider and be updated by
	 * changes to the data stream. f_modelListeners tracks all the model
	 * listeners that are listening to this data stream.
	 */
	private final List<IModelListener<T>> f_modelListener = new ArrayList<IModelListener<T>>();
	/**
	 * Used for logging info and instance creation.
	 * 
	 * @see #loadDataBaseList(String, ISqlSelectorCallBack)
	 */
	private final Class<T> f_class;
	/**
	 * If the Class being stored implments {@link IBlockDataExt} then f_expanded
	 * will be {@code true}
	 */
	private boolean f_expanded;
	private IComplexDataHooks f_hooks;
	private boolean f_tracing;
	private boolean f_debugging;
	private Set<BlockMod<T>> f_blockKeyedUpdateData;
	/**
	 * When data errors occur during updates, try to continue processing.
	 */
	private boolean f_continueProcessing;

	public BlockProvider(Class<T> p_class) {
		f_class = p_class;
		f_tracing = CoreInterface.getSystem().traceActive(p_class, TRACEBLOCK);
		f_debugging = CoreInterface.getSystem().traceActive(p_class, DEBUGBLOCK);
		for (final Class<?> l_interface : p_class.getInterfaces()) {
			if (l_interface.equals(IBlockDataExt.class)) {
				f_expanded = true;
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private IComplexDataHooks getHooks() {
		if (f_hooks == null) {
			f_hooks = ComplexDataHooks.getInstance((Class<? extends IBlockDataExt<T>>) f_class);
		}
		return f_hooks;
	}

	/**
	 * @return the noDuplicateCheck
	 */
	public boolean isNoDuplicateCheck() {
		return f_noDuplicateCheck;
	}

	/**
	 * @param noDuplicateCheck
	 *            the noDuplicateCheck to set
	 */
	public void setNoDuplicateCheck(boolean p_noDuplicateCheck) {
		this.f_noDuplicateCheck = p_noDuplicateCheck;
	}

	/**
	 * @return the modelListener
	 */
	public List<IModelListener<T>> getModelListeners() {
		return f_modelListener;
	}

	public void addModelListener(IModelListener<T> p_modelListener) {
		if (!f_modelListener.contains(p_modelListener)) {
			f_modelListener.add(p_modelListener);
		}
	}

	public void removeModelListener(IModelListener<T> p_modelListener) {
		f_modelListener.remove(p_modelListener);
	}

	/**
	 * Helper method for adding content directly onto the update data set
	 *
	 * @param p_oldData
	 * @param p_newData
	 */
	public void addBlockUpdateData(T p_oldData, T p_newData) {
		final BlockMod<T> l_modData = new BlockMod<T>(p_oldData, p_newData);
		if (!f_blockUpdateData.contains(l_modData)) {
			trace("Adding data to modify block: %s modifies %s with %s", this, p_oldData, p_newData);
			f_blockUpdateData.add(l_modData);
		}
	}

	@Override
	public void startBlock() {
		if (!f_blockOn) {
			debug("Start block: " + this);
			f_blockOn = true;
			f_blockData.clear();
			f_blockRemoveData.clear();
			f_blockUpdateData.clear();
			if (f_expanded) {
				getHooks().startBlock();
			}
		}
	}

	@Override
	public void cancelBlock() {
		f_blockOn = false;
	}

	/**
	 * this internal method does an actual update to the database when not in
	 * block mode
	 *
	 * @param p_ac
	 * @param p_data
	 * @return
	 */
	protected boolean addContent(final Connection p_ac, final T p_data) {
		boolean l_retVal = false;
		trace("Adding content [internal]: " + this + " adds " + p_data);
		final long l_insertFields = -1 & ~p_data.autoIncrements() & ~p_data.readOnlyFields();
		try (PreparedStatement l_ps = p_ac.prepareStatement(INSERTPRE + p_data.tableName() + ENDQUOTE
				+ p_data.insertHead(l_insertFields) + VALUESSTR + p_data.insertValues(l_insertFields),
				Statement.RETURN_GENERATED_KEYS)) {
			p_data.insertValueCallBack(l_insertFields, 1, l_ps);
			trace("Adding sql: " + l_ps);
			l_ps.executeUpdate();
			processAutoIncrements(l_ps, p_data);
			l_retVal = true;
		} catch (final SQLException l_e) {
			CoreInterface.getSystem().handleException(null, l_e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	protected void processAutoIncrements(PreparedStatement p_ps, IBlockData p_data) throws SQLException {
		try (ResultSet l_rs = p_ps.getGeneratedKeys()) {
			if (l_rs != null && l_rs.next()) {
				final int l_cols = l_rs.getMetaData().getColumnCount();
				for (int l_colCnt = 0; l_colCnt != l_cols; l_colCnt++) {
					final long l_newId = l_rs.getLong(l_colCnt + 1);
					trace("Autoincrement Field set: %d set to %d", l_colCnt, l_newId);
					p_data.autoIncrementField(l_colCnt, l_newId);
				}
			}
		}
	}

	protected boolean removeContentImmediate(final T p_data) {
		boolean l_retVal = false;
		trace("Removing content [internal]: " + this + " removes " + p_data);
		try (ExceptionCollector<Boolean> p_e = new BlockProviderCollector(
				String.format(BLOCKERROR, REMOVEERROR, p_data.tableName(), p_data), f_continueProcessing)) {
			l_retVal = p_e.call(new RemoveImmediateCall<T>(p_data, this), false);
		}
		return l_retVal;
	}

	protected boolean updateContent(final Connection p_conn, final T p_oldData, final T p_newData) throws SQLException {
		trace("Updating content [internal]: %s modifies %s with %s", this, p_oldData, p_newData);
		try (PreparedStatement l_ps = p_conn.prepareStatement("UPDATE `" + p_oldData.tableName() + ENDQUOTE
				+ p_oldData.modifyBody(p_newData) + " WHERE " + p_oldData.selectClause("", false))) {
			p_newData.insertValueCallBack(p_oldData.modified(p_newData), 1, l_ps);
			trace("Updating sql: " + l_ps);
			l_ps.executeUpdate();
		}
		return true;
	}

	@Override
	public boolean endBlock() {
		return endBlock(true);
	}

	@Override
	public boolean endBlock(boolean p_useDeleteClause) {
		boolean l_retVal = noUpdate();
		if (!l_retVal) {
			try (ExceptionCollector<Boolean> p_e = new BlockProviderCollector("Block Updates", f_continueProcessing)) {
				l_retVal = p_e.call(new EndBlockCall<T>(p_useDeleteClause, this), false);
			}
		}
		return l_retVal;
	}

	/**
	 * Return true if there is no data to be updated.
	 * 
	 * @return
	 */
	private boolean noUpdate() {
		return f_blockData.isEmpty() && f_blockRemoveData.isEmpty() && f_blockUpdateData.isEmpty();
	}

	protected void endBlockSetup(Connection p_connection) throws SQLException {
		if (f_expanded) {
			final Date l_endTime = new Date();
			debug("Starting block processing on " + this.getClass().getCanonicalName() + " at " + l_endTime);
			getHooks().startEndBlock(p_connection);
		}
	}

	protected void endBlockFinish(Connection p_connection) throws SQLException {
		if (f_expanded) {
			final Date l_startTime = new Date();
			debug("Ending block processing on " + this.getClass().getCanonicalName() + " at " + l_startTime);
			getHooks().finishEndBlock(p_connection);
		}
	}

	/**
	 * Process all block removes, updates and adds. We do them in that order so
	 * there are no issues with duplicates, etc. As long as the blocks are
	 * properly formed :)
	 *
	 * @param conn
	 *            Connection that the SQL database lives at
	 * @param useDeleteClause
	 *            Tell the system to use the IBlockData's sql delete clause to
	 *            filter what data can be deleted.
	 * @return if the data updates are successful true is returned, otherwise
	 *         false
	 * @throws SQLException
	 */
	public boolean endBlock(Connection p_conn, boolean p_useDeleteClause) throws SQLException {
		boolean l_retVal = false;
		if (f_blockOn) {
			f_blockOn = false;
			trace("End block: " + this);
			endBlockSetup(p_conn);
			/*
			 * first pass on updates to see if any needed to be added/removed
			 */
			assignIds(p_conn);
			final long l_fieldsToMod = updateCheck();
			removeEntries(p_conn, p_useDeleteClause);
			removeUpdateEntries(p_conn);
			final Set<BlockMod<T>> l_savedUpdate = new HashSet<BlockMod<T>>(f_blockUpdateData);
			final Set<T> l_savedAdds = new HashSet<T>(f_blockData);
			while (dataToUpdate(l_fieldsToMod)) {
				updateEntries(l_fieldsToMod, p_conn);
				insertEntries(p_conn);
			}
			f_blockUpdateData = l_savedUpdate;
			f_blockData = l_savedAdds;
			if (f_blockKeyedUpdateData != null) {
				for (final BlockMod<T> l_mod : f_blockKeyedUpdateData) {
					f_blockData.remove(l_mod.getNewValue());
				}
				f_blockUpdateData.addAll(f_blockKeyedUpdateData);
			}
			f_blockKeyedUpdateData = null;
			endBlockFinish(p_conn);
			l_retVal = true;
		}
		trace("End Block processing finished on : " + f_class.getName());
		return l_retVal;
	}

	private boolean dataToUpdate(long p_fieldsToMod) {
		return !f_blockData.isEmpty() || !f_blockUpdateData.isEmpty() && p_fieldsToMod != 0;
	}

	private long updateCheck() {
		long l_fieldsToMod = 0;
		if (!f_blockUpdateData.isEmpty()) {
			final Iterator<BlockMod<T>> l_modIter = f_blockUpdateData.iterator();
			f_blockKeyedUpdateData = new HashSet<BlockMod<T>>();
			while (l_modIter.hasNext()) {
				final BlockMod<T> l_modData = l_modIter.next();
				/*
				 * determine the data fields we need to update
				 */
				final long l_modFields = l_modData.getOldValue().modified(l_modData.getNewValue());
				if ((l_modFields & l_modData.getOldValue().keyFields()) != 0) {
					f_blockKeyedUpdateData.add(l_modData);
					f_blockData.add(l_modData.getNewValue());
					l_modIter.remove();
				} else {
					l_fieldsToMod |= l_modData.getOldValue().modified(l_modData.getNewValue());
				}
			}
			trace("End block fields to modify: " + Long.toHexString(l_fieldsToMod));
		}
		return l_fieldsToMod;
	}

	protected Savepoint initializeConnection(Connection p_conn) throws SQLException {
		p_conn.setAutoCommit(false);
		trace("End block save point: " + this);
		return p_conn.setSavepoint();
	}

	protected void insertEntries(Connection p_conn) throws SQLException {
		/*
		 * insert all the entries
		 */
		if (!f_blockData.isEmpty()) {
			/*
			 * Work around for silent failure of .remove() on the iterator if the has value has changed.
			 */
			f_blockData = new HashSet<T>(f_blockData);
			int l_blockCount = 0;
			final List<T> l_currentAdds = new ArrayList<T>(f_blockSize);
			final StringBuilder l_sb = new StringBuilder();
			final Iterator<T> l_dataIter = f_blockData.iterator();
			while (l_dataIter.hasNext()) {
				final T l_data = l_dataIter.next();
				if (validUpdate(l_data)) {
					l_dataIter.remove();
					l_blockCount = insertEntriesIndividual(p_conn, l_blockCount, l_currentAdds, l_data, l_sb);
				}
			}
			if (l_blockCount > 0) {
				doAddSQL(l_sb.toString(), l_currentAdds, p_conn);
			}
		}
	}

	private int insertEntriesIndividual(Connection p_conn, int p_blockCount, List<T> p_currentAdds, T p_data,
			StringBuilder p_sb) throws SQLException {
		int l_retVal = p_blockCount;
		final long l_insertFields = -1 & ~p_data.autoIncrements() & ~p_data.readOnlyFields();
		final String l_valuesString = p_data.insertValues(l_insertFields);
		if (p_blockCount == 0) {
			p_sb.append(String.format("INSERT INTO `%s` %s VALUES %s", p_data.tableName(),
					p_data.insertHead(l_insertFields), l_valuesString));
		} else {
			p_sb.append(", " + l_valuesString);
		}
		p_currentAdds.add(p_data);
		if (++l_retVal >= f_blockSize) {
			doAddSQL(p_sb.toString(), p_currentAdds, p_conn);
			p_currentAdds.clear();
			p_sb.delete(0, p_sb.length());
			l_retVal = 0;
		}
		return l_retVal;
	}

	/**
	 * When data is self referential, you may have to postpone updates to the
	 * database until the referenced data exists (ie has an ID.) If this is the
	 * case, overwrite this method to return false if the data is not ready to
	 * be saved.
	 *
	 * @param p_data
	 *            not null, the data to be checked
	 * @return true if the data is ready to be store, otherwise false.
	 */
	protected boolean validUpdate(T p_data) {
		return true;
	}

	protected void updateEntries(long p_fieldsToMod, Connection p_conn) throws SQLException {
		/*
		 * update all the entries
		 */
		long l_fieldsToMod = p_fieldsToMod;
		if (!f_blockUpdateData.isEmpty() && p_fieldsToMod != 0) {
			if (f_blockUpdateData.size()<=4) {
				final Iterator<BlockMod<T>> l_iterData = f_blockUpdateData.iterator();
				while (l_iterData.hasNext()) {
					final BlockMod<T> l_modData = l_iterData.next();
					if (validUpdate(l_modData.getNewValue())) {
						updateContent(p_conn, l_modData.getOldValue(), l_modData.getNewValue());
						l_iterData.remove();
					}
				}
			} else {
				updateEntriesViaTempTable(p_conn, l_fieldsToMod);
			}
		}
	}

	/**
	 * Create a temporary table to store a large number of changes.
	 * <br>
	 * When called, f_blockUpdateData is guarenteed to be not empty.
	 * 
	 * @param p_conn
	 * @param l_fieldsToMod
	 * @throws SQLException
	 */
	private void updateEntriesViaTempTable(Connection p_conn, long l_fieldsToMod) throws SQLException {
		/*
		 * load Temporary table with selecting data and modified data
		 */
		boolean l_tempTableExists = false;
		final T l_oData = f_blockUpdateData.iterator().next().getOldValue();
		l_fieldsToMod |= l_oData.keyFields();
		/*
		 * No temporary string created so use s_log's filter
		 */
		trace("End block mod Temorary Table created");
		try (Statement l_statement = p_conn.createStatement()) {
			l_statement.executeUpdate("CREATE TEMPORARY TABLE airsModify SELECT "
					+ l_oData.fieldNames(l_fieldsToMod) + " FROM `" + l_oData.tableName() + "` WHERE 1=0");
			l_tempTableExists = true;
			processUpdateTable(l_fieldsToMod, p_conn);
			final String l_updateSql = "UPDATE `" + l_oData.tableName() + "` dest inner join airsModify src" + " "
					+ l_oData.keyJoin() + " SET " + l_oData.fieldUpdates(l_fieldsToMod);
			/*
			 * update Table with modified data in temporary table
			 */
			trace("End block modify from temporary table:" + l_updateSql);
			try (Statement l_statement2 = p_conn.createStatement()) {
				int l_ret = l_statement2.executeUpdate(l_updateSql);
				System.out.println(l_ret);
			}
		} finally {
			/*
			 * remove temporary Table
			 */
			removeTemporaryTable(p_conn, l_tempTableExists);
		}
	}

	private void removeTemporaryTable(Connection p_conn, boolean p_tempTableExists) throws SQLException {
		trace("End block mod Temorary Table removed");
		if (p_tempTableExists) {
			try (Statement l_statement = p_conn.createStatement()) {
				l_statement.executeUpdate("DROP TEMPORARY TABLE airsModify");
			}
		}
	}

	protected void processUpdateTable(long p_fieldsToMod, Connection p_conn) throws SQLException {
		StringBuilder l_sb = new StringBuilder();
		int l_blockCount = 0;
		final List<T> l_currentBlock = new ArrayList<T>(f_blockSize);
		final Iterator<BlockMod<T>> l_iterData = f_blockUpdateData.iterator();
		while (l_iterData.hasNext()) {
			final BlockMod<T> l_modData = l_iterData.next();
			if (validUpdate(l_modData.getNewValue())) {
				final String l_valuesString = l_modData.getNewValue().insertValues(p_fieldsToMod);
				if (l_blockCount == 0) {
					l_sb.append("INSERT INTO airsModify " + l_modData.getNewValue().insertHead(p_fieldsToMod)
							+ VALUESSTR + l_valuesString);
				} else {
					l_sb.append(", " + l_valuesString);
				}
				l_currentBlock.add(l_modData.getNewValue());
				if (++l_blockCount >= f_blockSize) {
					doModSQL(l_sb.toString(), l_currentBlock, p_fieldsToMod, p_conn);
					l_blockCount = 0;
					l_currentBlock.clear();
					l_sb = new StringBuilder();
				}
				l_iterData.remove();
			}
		}
		if (l_blockCount > 0) {
			doModSQL(l_sb.toString(), l_currentBlock, p_fieldsToMod, p_conn);
		}
	}

	protected void removeEntries(Connection p_conn, boolean p_useDeleteClause) throws SQLException {
		/*
		 * remove all the entries
		 */
		if (!f_blockRemoveData.isEmpty()) {
			try (Statement l_statement = p_conn.createStatement()) {
				processRemoveTable(l_statement, f_blockRemoveData, p_useDeleteClause);
			}
		}
	}

	private void removeUpdateEntries(Connection p_conn) throws SQLException {
		/*
		 * remove all the keyed update entries
		 */
		if (f_blockKeyedUpdateData != null && !f_blockKeyedUpdateData.isEmpty()) {
			final Set<T> l_originals = getBlockUpdateOriginals(f_blockKeyedUpdateData);
			try (Statement l_statement = p_conn.createStatement()) {
				processRemoveTable(l_statement, l_originals, false);
			}
		}
	}

	/**
	 * Remove all the data in p_removeData from the data store.
	 * 
	 * @param p_statement  not null, the Statement to process removals with
	 * @param p_blockRemoveData  not null, List of the data to be removed
	 * @param p_useDeleteClause  boolean, 
	 * @throws SQLException
	 */
	private void processRemoveTable(Statement p_statement, Set<T> p_blockRemoveData, boolean p_useDeleteClause)
			throws SQLException {
		StringBuilder l_sb = new StringBuilder();
		int l_blockCount = 0;
		for (final T l_remData : p_blockRemoveData) {
			final String l_selectString = l_remData.selectClause("", p_useDeleteClause);
			if (l_blockCount == 0) {
				l_sb.append("DELETE FROM `" + l_remData.tableName() + "` WHERE (" + l_selectString + ")");
			} else {
				l_sb.append(" or (" + l_selectString + ")");
			}
			if (++l_blockCount >= f_blockSize) {
				trace("End block removing sql: " + l_sb.toString());
				p_statement.executeUpdate(l_sb.toString());
				l_blockCount = 0;
				l_sb = new StringBuilder();
			}
		}
		if (l_blockCount > 0) {
			trace("End block removing sql: " + l_sb.toString());
			p_statement.executeUpdate(l_sb.toString());
		}
	}

	protected void doModSQL(String p_sqlString, List<T> p_currentBlock, long p_fieldsToMod, Connection p_conn)
			throws SQLException {
		try (PreparedStatement l_ps = p_conn.prepareStatement(p_sqlString)) {
			int curOffset = 1;
			for (final T nData : p_currentBlock) {
				curOffset = nData.insertValueCallBack(p_fieldsToMod, curOffset, l_ps);
			}
			trace("End block insert to Temorary Table: " + l_ps);
			l_ps.executeUpdate();
		}
	}

	protected void doAddSQL(String p_sqlString, List<T> p_currentAdds, Connection p_conn) throws SQLException {
		try (ExceptionCollector<Boolean> l_pe = new BlockProviderCollector("Adding records", f_continueProcessing)) {
			l_pe.call(new AddRecordsCall<T>(p_conn, p_sqlString, p_currentAdds, this), false);
		}
	}

	protected void processGeneratedKeys(PreparedStatement l_ps, List<T> p_currentAdds)
			throws SQLException {
		try (ResultSet l_rs = l_ps.getGeneratedKeys()) {
			if (l_rs != null) {
				final int l_cols = l_rs.getMetaData().getColumnCount();
				int l_cnt = 0;
				while (l_rs.next()) {
					final T l_ca = p_currentAdds.get(l_cnt);
					generatedKeyUpdate(l_cols, l_ca, l_rs);
					l_cnt++;
				}
			}
		}
	}

	private void generatedKeyUpdate(int p_cols, T p_ca, ResultSet p_rs) throws SQLException {
		for (int l_colCnt = 0; l_colCnt != p_cols; l_colCnt++) {
			final long l_incValue = p_rs.getLong(l_colCnt + 1);
			trace("End block autoincrement field modified " + l_colCnt + " to " + l_incValue);
			p_ca.autoIncrementField(l_colCnt, l_incValue);
		}
	}

	protected void insertCallBacks(List<T> p_currentAdds, PreparedStatement p_ps) throws SQLException {
		int curOffset = 1;
		for (final T l_nData : p_currentAdds) {
			curOffset = l_nData.insertValueCallBack(-1, curOffset, p_ps);
		}
	}

	/**
	 * @return the blockSize
	 */
	public int getBlockSize() {
		return f_blockSize;
	}

	/**
	 * @param blockSize
	 *            the blockSize to set
	 */
	public void setBlockSize(int p_blockSize) {
		this.f_blockSize = p_blockSize;
	}

	/**
	 * @return the blockOn
	 */
	@Override
	public boolean isBlockOn() {
		return f_blockOn;
	}

	/**
	 * @param blockData
	 *            the blockData to set
	 */
	protected void setBlockData(Set<T> p_blockData) {
		this.f_blockData = p_blockData;
	}

	/**
	 * @param blockRemoveData
	 *            the blockRemoveData to set
	 */
	protected void setBlockRemoveData(Set<T> p_blockRemoveData) {
		this.f_blockRemoveData = p_blockRemoveData;
	}

	/**
	 * @param blockUpdateData
	 *            the blockUpdateData to set
	 */
	protected void setBlockUpdateData(Set<BlockMod<T>> p_blockUpdateData) {
		this.f_blockUpdateData = p_blockUpdateData;
	}

	/**
	 * @param blockOn
	 *            the blockOn to set
	 */
	@Override
	public void setBlockOn(boolean p_blockOn) {
		this.f_blockOn = p_blockOn;
	}

	/**
	 * @return the blockData
	 */
	@Override
	public Set<T> getBlockData() {
		return f_blockData;
	}

	/**
	 * @return the blockRemoveData
	 */
	@Override
	public Set<T> getBlockRemoveData() {
		return f_blockRemoveData;
	}

	/**
	 * @return the blockUpdateData
	 */
	@Override
	public Set<BlockMod<T>> getBlockUpdateData() {
		return f_blockUpdateData;
	}

	@Override
	public Set<T> getBlockUpdateOriginals() {
		return getBlockUpdateOriginals(f_blockUpdateData);
	}

	private Set<T> getBlockUpdateOriginals(Set<BlockMod<T>> p_blockUpdateData) {
		final Set<T> l_retVal = new HashSet<T>();
		for (final BlockMod<T> bm : p_blockUpdateData) {
			l_retVal.add(bm.getOldValue());
		}
		return l_retVal;
	}

	@Override
	public T loadContent(T p_data, boolean p_update) {
		T l_retVal = p_data;
		for (final IModelListener<T> l_listener : getModelListeners()) {
			l_retVal = l_listener.loadContent(p_data, p_update);
			if (l_retVal == null || !l_retVal.equals(p_data)) {
				break;
			}
		}
		return l_retVal;
	}

	@Override
	public boolean addContent(T p_data) {
		boolean l_retVal = true;
		if (f_expanded && !isBlockOn()) {
			try {
				startBlock();
				l_retVal = addContent(p_data);
				endBlock();
			} finally {
				cancelBlock();
			}
		} else {
			final boolean loadContentCheck = f_noDuplicateCheck || !isBlockOn() || !f_blockData.contains(p_data);
			p_data.dataAdded();
			if (loadContentCheck && loadContent(p_data, true) == p_data) {
				trace("Adding content: " + this + " adds " + p_data);
				if (isBlockOn()) {
					addRecordHook(p_data);
					f_blockData.add(p_data);
				} else {
					l_retVal = addContentImmediate(p_data);
				}
			}
		}
		return l_retVal;
	}

	protected boolean addContentImmediate(final T p_data) {
		boolean l_retVal = true;
		try (ExceptionCollector<Boolean> p_exceptionCollector = new BlockProviderCollector(
				String.format(BLOCKERROR, ADDERROR, p_data.tableName(), ""), f_continueProcessing)) {
			l_retVal = p_exceptionCollector.call(new AddImmediateCall<T>(p_data, this), false);
		}
		return l_retVal;
	}

	@Override
	public boolean removeContent(T p_data) {
		boolean rVal = true;
		trace("Removing content: %s removes %s", this, p_data);
		if (isBlockOn()) {
			if (!f_blockRemoveData.contains(p_data)) {
				f_blockRemoveData.add(p_data);
			}
		} else {
			if (f_expanded) {
				startBlock();
				removeContent(p_data);
				endBlock();
			} else {
				rVal = removeContentImmediate(p_data);
			}
		}
		return rVal;
	}

	@Override
	public boolean updateContent(T p_oldData, T p_newData) {
		boolean rVal = true;
		// this is a bit redundant ... perhaps just do the modified check
		final long l_modified = p_oldData.modified(p_newData);
		if (l_modified != 0) {
			trace("Updating content: " + this + " updates " + p_oldData + " with " + p_newData);
			if (isBlockOn()) {
				modifyRecordHook(p_oldData, p_newData);
				addBlockUpdateData(p_oldData, p_newData);
			} else {
				// if key fields are modified we need to do a block update
				if (f_expanded || (l_modified & p_oldData.keyFields()) != 0) {
					startBlock();
					updateContent(p_oldData, p_newData);
					endBlock();
				} else {
					rVal = updateContentImmediate(p_oldData, p_newData);
				}
			}
		}
		return rVal;
	}

	protected void trace(String p_string, Object... p_args) {
		if (f_tracing) {
			CoreInterface.getSystem().trace(f_class, TRACEBLOCK,
					p_args.length > 0 ? String.format(p_string, p_args) : p_string);
		}
	}

	protected void debug(String p_string, Object... p_args) {
		if (f_debugging) {
			CoreInterface.getSystem().debug(f_class, DEBUGBLOCK,
					p_args.length > 0 ? String.format(p_string, p_args) : p_string);
		}
	}

	protected boolean updateContentImmediate(T p_oldData, T p_newData) {
		boolean l_retVal = true;
		try (ExceptionCollector<Boolean> p_e = new BlockProviderCollector(
				String.format(BLOCKERROR, UPDATEERROR, p_oldData.tableName(), ""), f_continueProcessing)) {
			l_retVal = p_e.call(new UpdateImmediateCall<T>(p_oldData, p_newData, this), false);
		}
		return l_retVal;
	}

	/**
	 * most databases don't need to preassign IDs. But in the case of graphs and
	 * cyclical data, it is necessary to add the IDs
	 */
	@Override
	public void assignIds(Connection p_conn) throws SQLException {
		// The default for this hook function is to do nothing
	}

	@Override
	public boolean canEdit(T p_data) {
		return true;
	}

	@SuppressWarnings("unchecked")
	protected void addRecordHook(T p_data) {
		if (f_expanded) {
			((IBlockDataExt<T>) p_data).addRecord();
		}
	}

	@SuppressWarnings("unchecked")
	protected void modifyRecordHook(T p_data, T p_newData) {
		if (f_expanded) {
			((IBlockDataExt<T>) p_newData).modifyRecord(p_data);
		}
	}

	@SuppressWarnings("unchecked")
	protected void removeRecordHook(T p_data) {
		if (f_expanded) {
			((IBlockDataExt<T>) p_data).removeRecord();
		}
	}

	public void setTracing(boolean p_tracing) {
		f_tracing = p_tracing;
	}

	public void setDebugging(boolean p_debugging) {
		f_debugging = p_debugging;
	}

	/**
	 * @return the tracing
	 */
	public boolean isTracing() {
		return f_tracing;
	}

	/**
	 * @return the debugging
	 */
	public boolean isDebugging() {
		return f_debugging;
	}

	public boolean loadDataBase(String p_selectionString, ISqlSelectorCallBack p_callBack) {
		final Set<T> l_newData = loadDataBaseList(p_selectionString, p_callBack);
		for (final IModelListener<T> l_model : f_modelListener) {
			l_model.addData(l_newData);
		}
		return true;
	}

	public Set<T> loadDataBaseList(String p_selectionString, ISqlSelectorCallBack p_callBack) {
		return loadDataBaseListExt(p_selectionString, p_callBack, -1);
	}

	public Set<T> loadDataBaseListExt(String p_selectionString, ISqlSelectorCallBack p_callBack, long p_fields) {
		final Set<T> l_newData = new HashSet<T>();
		try (Connection l_connection = CoreInterface.getSystem().getConnection();
				PreparedStatement l_ps = l_connection.prepareStatement(p_selectionString)) {
			if (p_callBack != null) {
				p_callBack.loadSelectionString(l_ps);
			}
			trace(l_ps.toString());
			final ResultSet l_rs = l_ps.executeQuery();
			processRecord(p_fields, l_newData, l_rs);
		} catch (SQLException | InstantiationException | IllegalAccessException | ParseException e) {
			CoreInterface.getSystem().handleException("Unable to load database", e, NotificationStatus.BLOCK);
		}
		return l_newData;
	}

	protected void processRecord(long p_fields, Set<T> l_newData, ResultSet l_rs)
			throws SQLException, InstantiationException, IllegalAccessException, ParseException {
		while (l_rs.next()) {
			final T l_newObject = f_class.newInstance();
			int l_index = 0;
			for (final String l_fieldName : l_newObject.getFieldNames()) {
				if ((p_fields & 1 << l_index) != 0) {
					l_newObject.toField(l_index++).fromSqlValue(l_rs.getString(l_fieldName));
				}
			}
			l_newData.add(l_newObject);
		}
	}

	public Class<T> getDataClass() {
		return f_class;
	}

	/**
	 * @return the continueProcessing
	 */
	public boolean isContinueProcessing() {
		return f_continueProcessing;
	}

	/**
	 * @param p_continueProcessing
	 *            the continueProcessing to set
	 */
	public void setContinueProcessing(boolean p_continueProcessing) {
		f_continueProcessing = p_continueProcessing;
	}

}
