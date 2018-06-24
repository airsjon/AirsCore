/**
 *
 */
package com.airsltd.core.model;

import com.airsltd.core.data.IBlockData;
import com.airsltd.core.model.BlockModel;

/**
 * This Factory implementation allows an project using com.airsltd.ga to create
 * their own factory. For example, in an E4 application, we may want the
 * associated instance to be store in the context. Allowing for more complex
 * provider system.
 *
 * @author Jon Boley
 *
 */
public interface IBlockModelFactory {

	/**
	 * Return the current BlockModel<T,V> for T and V. If one does not exist
	 * create a new one.
	 *
	 * @param p_class
	 * @return
	 */
	<T extends IBlockData, V> BlockModel<T, V> getInstance(Class<T> p_class, Class<V> p_inputClass);

	/**
	 * This allows a class that extends BlockModel<T,V> to be available for T
	 * and V on the singleton store
	 *
	 * @param p_class
	 * @param p_instance
	 */
	<T extends IBlockData, V> void setInstance(Class<T> p_class, Class<V> p_inputClass, BlockModel<T, V> p_instance);

}
