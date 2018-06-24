/**
 *
 */
package com.airsltd.core.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jon_000
 *
 */
public interface ISqlSelectorCallBack {

	void loadSelectionString(PreparedStatement p_ps) throws SQLException;

}
