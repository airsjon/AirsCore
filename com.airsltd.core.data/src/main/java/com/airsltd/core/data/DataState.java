/**
 *
 */
package com.airsltd.core.data;

/**
 * The state of the data.
 *
 * @author Jon Boley
 *
 */
public enum DataState {
	/**
	 * The data should be modified in the database
	 */
	VALID, 
	/**
	 * The data has already been modified in the database so this change
	 * can be dropped
	 */
	REDUNDENT, 
	/**
	 * Conflict has occurred.
	 * <p>
	 * In this case, an issue is generated showing the existing
	 * change and the requested change. The TD can then determine
	 * which changes to enact. Conflicts can only occur with data
	 * that is being modified.
	 */
	CONFLICT

}
