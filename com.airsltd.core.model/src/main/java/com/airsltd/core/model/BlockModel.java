/**
 * Copyright (c) 2013 Jon Boley
 */
package com.airsltd.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.BlockMod;
import com.airsltd.core.data.BlockProvider;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.IBlockData;
import com.airsltd.core.data.IModelListener;
import com.airsltd.core.data.ISqlSelectorCallBack;
import com.airsltd.core.data.IUserInterfaceListener;
import com.airsltd.core.data.converters.DatabaseConverter;

/**
 * Abstract base for Model instantiation.
 * <p>
 * The Model will return objects of T with a selector of V.
 *
 * @author Jon Boley
 *
 */
public abstract class BlockModel<T extends IBlockData, V> implements IModelListener<T> {

	private static Map<Object, IFromIdModel<? extends IBlockData>> s_models = new HashMap<>();

	private BlockProvider<T> f_blockProvider;
	protected List<IUserInterfaceListener<T>> f_uiListeners = new ArrayList<IUserInterfaceListener<T>>();
	private boolean f_initialLoad;
	private boolean f_noUIUpdate;
	private static Boolean s_tracing;

	static {
		DatabaseConverter.setConverter(new PersistentIdConverterFactory());
	}

	/**
	 * Create a BlockModel for class T.
	 * <p>
	 * 
	 * @param p_provider
	 *            not null, the provider to use for the model
	 * @param T
	 *            not null, the class the model will be storing
	 */
	public BlockModel(BlockProvider<T> p_provider) {
		super();
		f_blockProvider = p_provider;
		f_blockProvider.addModelListener(this);
	}

	public static <T extends IBlockData> IFromIdModel<T> getFromIdModel(Class<T> p_class) {
		@SuppressWarnings("unchecked")
		final IFromIdModel<T> l_retVal = (IFromIdModel<T>) s_models.get(p_class);
		if (l_retVal == null) {
			CoreInterface.getSystem().handleException("Unable to load model for " + p_class.getName(),
					null, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	/**
	 * Mainly used for debuging and Junit tests, this method allows the model
	 * associated with a class to be overwritten with <code>p_model</code>
	 *
	 * @param p_class
	 * @param p_model
	 */
	public static <T extends IBlockData> void setFromIdModel(Class<T> p_class, IFromIdModel<T> p_model) {
		s_models.put(p_class, p_model);
	}

	/**
	 * @return the blockProvider
	 */
	public BlockProvider<T> getBlockProvider() {
		return f_blockProvider;
	}

	/**
	 * Used mainly for testing. Allows for the blockprovider to be substitued
	 * with a mock provider.
	 *
	 * @param p_blockProvider
	 */
	public void setBlockProvider(BlockProvider<T> p_blockProvider) {
		f_blockProvider = p_blockProvider;
	}

	public void addUserInterface(IUserInterfaceListener<T> p_listener) {
		if (!f_uiListeners.contains(p_listener)) {
			f_uiListeners.add(p_listener);
		}
	}

	public void removeUserInterface(IUserInterfaceListener<T> p_listener) {
		f_uiListeners.remove(p_listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IModelListener#blockStart()
	 */
	@Override
	public void blockUIStart() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IModelListener#blockStop()
	 */
	@Override
	public void blockUIStop() {
	}

	/**
	 * Helper function to put p_data into a Set and add it.
	 * 
	 * @param p_data  not null, the data to add.
	 */
	public final void addData(T p_data) {
		addData(Arrays.asList(p_data));
	}
	
	/**
	 * Helper method to add a collection of T data into the model.
	 * 
	 * @param p_addData
	 */
	final public void addData(Collection<T> p_addData) {
		addData(new HashSet<T>(p_addData));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IModelListener#addData(java.util.List)
	 */
	@Override
	public void addData(Set<T> p_addData) {
		addUIData(addModelData(p_addData));
	}

	public void addUIData(Set<T> p_addData) {
		if (!f_noUIUpdate) {
			for (final IUserInterfaceListener<T> l_uiListener : f_uiListeners) {
				l_uiListener.addData(p_addData);
			}
		}
	}

	public void modifyUIData(T p_oldData, T p_newData) {
		if (!f_noUIUpdate) {
			for (final IUserInterfaceListener<T> l_uiListener : f_uiListeners) {
				l_uiListener.modifyData(Arrays.asList(p_oldData),
						new HashSet<String>(p_oldData.updateProperties(p_newData)));
			}
		}
	}

	/**
	 * Helper method to add a collection of data into the model.
	 * 
	 * @param p_addData
	 * @return
	 */
	final public Set<T> addModelData(Collection<T> p_addData) {
		return addModelData(new HashSet<T> (p_addData));
	}
	/**
	 * <p>
	 * This abstract method is overwritten by the Model object to make any
	 * changes to the model after the list of T have been added to the database
	 * (persistent store). The list is then returned (filtered if need be) for
	 * displaying to the UI layer. Any items that the model does not want to
	 * share with the UI layer should be removed from the list being returned.
	 * </p>
	 * <p>
	 * Note: Adding data to the mode may need to be filtered. Modification and
	 * Removal will only effect the UI layer if the object is already being
	 * displayed (and, therefor, already filtered.)
	 * </p>
	 *
	 * @param p_addData
	 * @return
	 */
	public abstract Set<T> addModelData(Set<T> p_addData);

	/**
	 * Helper method to remove a single data record.
	 * 
	 * @param p_remData
	 */
	public final void removeData(T p_remData) {
		removeData(new HashSet<T>(Arrays.asList(p_remData)));
	}

	/**
	 * Helper method to remove a collection data T.
	 * 
	 * @param p_remData  not null, the collection of data to remove
	 */
	public final void removeData(Collection<T> p_remData) {
		removeData(new HashSet<T>(p_remData));
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IModelListener#updateData(java.util.List)
	 */
	@Override
	public final Set<String> prepareUpdateData(Set<BlockMod<T>> p_data, List<IUserInterfaceListener<T>> p_listeners) {
		final Set<String> l_properties = new HashSet<String>();
		if (!p_data.isEmpty()) {
			final List<T> modifiedList = new ArrayList<T>();
			for (final BlockMod<T> modData : p_data) {
				final List<String> l_itemProperties = modData.getOldValue().updateProperties(modData.getNewValue());
				if (l_itemProperties != null) {
					l_properties.addAll(l_itemProperties);
				}
				copyData(modData.getOldValue(), modData.getNewValue());
				modifiedList.add(modData.getOldValue());
			}
			for (final IUserInterfaceListener<T> l_uiListener : f_uiListeners) {
				p_listeners.add(l_uiListener);
			}
		}
		return l_properties;
	}

	@Override
	public void notifyListeners(List<IUserInterfaceListener<T>> p_listeners, Set<String> p_properties,
			List<T> p_dataModified) {
		for (final IUserInterfaceListener<T> l_listener : p_listeners) {
			l_listener.modifyData(p_dataModified, p_properties);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.core.data.IModelListener#removeData(java.util.List)
	 */
	@Override
	public void removeData(Set<T> p_remData) {
		remModelData(p_remData);
		remUIData(p_remData);
	}

	public void remUIData(Iterable<T> p_remData) {
		if (!f_noUIUpdate) {
			for (final IUserInterfaceListener<T> l_uiListener : f_uiListeners) {
				l_uiListener.removeData(p_remData);
			}
		}
	}

	/**
	 * Helper method to remove a collection of data from the model.
	 * 
	 * @param p_remData
	 */
	public void remModelData(Collection<T> p_remData) {
		remModelData(new HashSet<T>(p_remData));
	}

	/**
	 * This abstract method is overwritten by the Model object to make any
	 * changes to the model after the list of T have been removed from the
	 * database (persistent store).
	 *
	 * @param p_remData
	 */
	public abstract void remModelData(Set<T> p_remData);

	/**
	 * This hook is called after a successful update of the data system. It is
	 * now time to update the data from the model
	 * 
	 * @param data
	 */
	public void copyData(T oldData, T newData) {
		oldData.copy(newData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.core.data.IModelListener#loadContent(com.airsltd.core.data.
	 * IBlockData, boolean)
	 */
	@Override
	public T loadContent(T p_data, boolean p_update) {
		return p_data;
	}

	/**
	 * This method is to be overridden by those model's that return an actual
	 * list of values
	 * 
	 * @param p_selector
	 * @return
	 */
	public List<T> getContentAsList(V p_selector) {
		return new ArrayList<T>();
	}

	public Set<T> getContentAsSet(V p_selector) {
		return new HashSet<T>();
	}

	// interfaces to block Provider

	public void startBlock() {
		f_blockProvider.startBlock();
	}

	public boolean endBlock() {
		return endBlock(false);
	}

	public boolean endBlock(boolean p_useDeleteClause) {
		return f_blockProvider.endBlock(p_useDeleteClause);
	}

	public void cancelBlock() {
		f_blockProvider.cancelBlock();
	}

	/**
	 * Override this method if you need to check the data before it is added to
	 * the persistent store.
	 *
	 * @param p_data
	 * @return
	 */
	public boolean addContent(T p_data) {
		return f_blockProvider.addContent(p_data);
	}

	/**
	 * Override this method if you need to check the data before it is removed.
	 *
	 * @param p_data
	 * @return
	 */
	public boolean removeContent(T p_data) {
		return f_blockProvider.removeContent(p_data);
	}

	/**
	 * Override this method if you need to check the data before it is updated.
	 * 
	 * @param p_oldData
	 * @param p_newData
	 * @return
	 */
	public boolean updateContent(T p_oldData, T p_newData) {
		return f_blockProvider.updateContent(p_oldData, p_newData);
	}

	public boolean isBlockOn() {
		return f_blockProvider.isBlockOn();
	}

	public static void trace(Object p_ps) {
		if (s_tracing == null) {
			s_tracing = CoreInterface.getSystem().traceActive(BlockModel.class, "/data");
		}
		if (s_tracing) {
			CoreInterface.getSystem().trace(BlockModel.class, "/data", p_ps.toString());
		}
	}

	protected static void clearTracing() {
		s_tracing = null;
	}

	/**
	 * @return the initialLoad
	 */
	public boolean isInitialLoad() {
		return f_initialLoad;
	}

	/**
	 * @param p_initialLoad
	 *            the initialLoad to set
	 */
	public void setInitialLoad(boolean p_initialLoad) {
		f_initialLoad = p_initialLoad;
	}

	/**
	 * Load the data from the persistent store.
	 * <p>
	 * This method is used to load the content from the data store. p_qualifer
	 * is a value that can be passed by application code to help determine what
	 * needs to be loaded. Note: loadModel should load over the data if it is
	 * called. This allows for refreshing of data.
	 *
	 * @param p_qualifier
	 * @return
	 */
	public boolean loadModel(V p_qualifier) {
		clearData(p_qualifier);
		final Set<T> p_loadedData = loadModelData(getSelectionQuery(p_qualifier), getSelectionCallBack(p_qualifier),
				getFieldsToLoad());
		addData(p_loadedData);
		return true;
	}

	protected long getFieldsToLoad() {
		return -1;
	}

	protected Set<T> loadModelData(String p_selectionQuery, ISqlSelectorCallBack p_selectionCallBack, long p_fields) {
		return !p_selectionQuery.isEmpty()
				? getBlockProvider().loadDataBaseListExt(p_selectionQuery, p_selectionCallBack, p_fields)
				: new HashSet<T>();
	}

	/**
	 * Hook method to provide an ISqlSelectorCallBack for the Selection string.
	 * <p>
	 * Some selection strings may required complicated data that is better using
	 * a call back.
	 *
	 * @param p_qualifier
	 * @return an {@link ISqlSelectorCallBack} or null. A null value represents
	 *         no call back method.
	 */
	protected ISqlSelectorCallBack getSelectionCallBack(V p_qualifier) {
		return null;
	}

	/**
	 * The query used to get the data associated with this model.
	 * <p>
	 * The default behavior is to return an empty string. An empty string means
	 * that no loading should be done, most likely a lazy loading database.
	 *
	 * @param p_qualifier
	 *            implementation dependent qualifier
	 * @return
	 */
	protected String getSelectionQuery(V p_qualifier) {
		return "";
	}

	/**
	 * Hook to allow for granulated clearing of data based on the incoming
	 * p_qualifier
	 * 
	 * @param p_qualifier
	 *            implementation dependent qualifier
	 */
	protected void clearData(V p_qualifier) {
	}

	public void setNoUIUpdate(boolean p_boolean) {
		f_noUIUpdate = p_boolean;
	}

	/**
	 * Helper method to run a block update with deletes forced.
	 * 
	 * @param p_method  not null, method to run inside the block update.
	 */
	public void doBlockUpdate(Runnable p_method) {
		doBlockUpdate(true, p_method);
	}

	/**
	 * Create a block update environment to run p_method inside.
	 * <br>
	 * This runs the code inside a try-final block with {@link #startBlock()} preceding the code.
	 * {@link #endBlock()} is executed after the code.
	 * {@link #cancelBlock()} is executed after the code is finished.
	 * 
	 * @param p_forceDelete
	 * @param p_method  not null, the code to run inside a block update
	 */
	public void doBlockUpdate(boolean p_forceDelete, Runnable p_method) {
		if (isBlockOn()) {
			p_method.run();
		} else {
			try {
				startBlock();
				p_method.run();
				endBlock(p_forceDelete);
			} finally {
				cancelBlock();
			}
		}
	}
}
