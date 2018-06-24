/**
 *
 */
package com.airsltd.core.data;

import java.util.HashMap;
import java.util.Map;

/**
 * This class acts as a factory for {@link IComplexDataHooks} by providing a
 * mechanism for saving and retrieveng them based on the class they are defined
 * for.
 *
 * @author Jon Boley
 *
 */
public class ComplexDataHooks {

	private static Map<Class<? extends IBlockDataExt<? extends IBlockData>>, IComplexDataHooks> s_factories = new HashMap<>();

	public static final IComplexDataHooks CLEAN = new CleanDataHooks();

	private ComplexDataHooks() {
	}

	public static <V extends IBlockData, T extends IBlockDataExt<V>> IComplexDataHooks getInstance(Class<T> p_class) {
		final IComplexDataHooks l_retVal = s_factories.get(p_class);
		if (l_retVal == null) {
			throw new AirsExtBlockInializationException(p_class);
		}
		return l_retVal;
	}

	public static <V extends IBlockData, T extends IBlockDataExt<V>> void setInstance(Class<T> p_class,
			IComplexDataHooks p_factory) {
		s_factories.put(p_class, p_factory);
	}

}
