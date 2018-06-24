/**
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author jon_000
 *
 */
public class AddRecordsCall<T extends IBlockData> implements Callable<Boolean> {

	private final Connection f_conn;
	private final String f_sqlString;
	private final List<T> f_currentAdds;
	private final BlockProvider<T> f_provider;

	public AddRecordsCall(Connection p_conn, String p_sqlString, List<T> p_currentAdds, BlockProvider<T> p_provider) {
		super();
		f_conn = p_conn;
		f_sqlString = p_sqlString;
		f_currentAdds = p_currentAdds;
		f_provider = p_provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public Boolean call() throws Exception {
		try (PreparedStatement l_ps = f_conn.prepareStatement(f_sqlString, Statement.RETURN_GENERATED_KEYS)) {
			f_provider.insertCallBacks(f_currentAdds, l_ps);
			f_provider.trace("End block add content sql: " + l_ps);
			f_provider.debug("SQL - Adding data");
			l_ps.executeUpdate();

			final long l_autos = f_currentAdds.get(0).autoIncrements();
			if (l_autos != 0) {
				f_provider.processGeneratedKeys(l_ps, f_currentAdds);
			}
		}
		return true;
	}

}
