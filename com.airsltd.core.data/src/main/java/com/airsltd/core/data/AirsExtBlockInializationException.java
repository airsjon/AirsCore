/**
 *
 */
package com.airsltd.core.data;

/**
 * The block data has not been set up correctly. This is a coding issue.
 *
 * @author Jon Boley
 *
 */
public class AirsExtBlockInializationException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = -5947686206032548272L;

	public <T extends IBlockDataExt<? extends IBlockData>> AirsExtBlockInializationException(Class<T> p_class) {
		super("Unable to find IComplexDataHooks for class: " + p_class.getName());
	}

}
