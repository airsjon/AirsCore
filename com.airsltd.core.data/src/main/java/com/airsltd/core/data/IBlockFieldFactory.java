/**
 *
 */
package com.airsltd.core.data;

/**
 * @author Jon
 *
 */
public interface IBlockFieldFactory {

	/**
	 * Return field data for the block data.
	 *
	 * @param p_blockData
	 * @return
	 */
	IBlockField<?> field(IBlockData p_blockData);

}
