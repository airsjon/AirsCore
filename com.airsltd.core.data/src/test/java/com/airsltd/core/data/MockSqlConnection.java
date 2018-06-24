/**
 * Copyright (c) 2013, Jon Boley
 * 2703 Wildrose CT
 * Bellingham, WA 98229
 *
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.airsltd.core.NotificationStatus;
import com.airsltd.core.data.AirsPooledConnection;
import com.airsltd.core.data.CoreInterface;
import com.airsltd.core.data.ISqlConnection;

/**
 * @author Jon Boley
 *
 */
public abstract class MockSqlConnection  {

	public MockSqlConnection() {
		mockInitialize();
	}

	protected void mockInitialize() {
		initializeConnection();
		copySQLData();
	}
	
	protected void copySQLData() {
		CoreInterface.getSystem().getLog().trace("Creating mock SQL database");
		String dBaseName = "airsMock_"+getMockDatabase();
		try (Connection ac = getConnection(false)) {
			try (PreparedStatement l_ps = ac.prepareStatement("DROP DATABASE IF EXISTS "+dBaseName)) {
				l_ps.execute();
			}
			try (PreparedStatement l_ps = ac.prepareStatement("CREATE DATABASE "+dBaseName)) {
				l_ps.execute();
			}
			for (String tableName : getTables()) {
				String nTable = dBaseName+"."+tableName;
				String oTable = getRealDatabase()+"."+tableName;
				try (PreparedStatement l_ps = ac.prepareStatement("CREATE TABLE "+nTable+" LIKE "+oTable)) {
					l_ps.execute();
				}
			}
			copyData(ac, dBaseName);
			createSqlFunctions(ac, dBaseName);
			try (PreparedStatement l_ps = ac.prepareStatement("USE "+dBaseName)) {
				l_ps.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CoreInterface.getSystem().getLog().info("Mock database created");
	}

	abstract protected void copyData(Connection p_ac, String p_dBaseName) throws SQLException;

	abstract protected void createSqlFunctions(Connection p_ac, String dBaseName) throws SQLException;

	public void dispose() {
		String dBaseName = "airsMock"+getMockDatabase();
		try (Connection ac = getConnection(false);
			 PreparedStatement l_ps = ac.prepareStatement("DROP DATABASE "+dBaseName)) {
			l_ps.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return the name of the Mock Database to be created (this name will be "airsMock"+what is returned)
	 * @return
	 */
	abstract protected String	getMockDatabase();
	/**
	 * Return the name of the Mock Database to be created (this name will be "airsMock"+what is returned)
	 * @return
	 */
	abstract protected String getRealDatabase();
	/**
	 * Return a list of all the tables to be copied into the Mock Database
	 * @return
	 */
	abstract protected String[] getTables();

	public Connection getConnection() {
		return getConnection(true);
	}
	
	public Connection getConnection(boolean p_use) {
		Connection l_retVal = null;
		try {
			l_retVal = AirsPooledConnection.getInstance().getConnection();
			if (p_use) {
				String dBaseName = "airsMock_"+getMockDatabase();
				try (PreparedStatement l_ps = l_retVal.prepareStatement("USE "+dBaseName)) {
					l_ps.execute();
				}
			}
		} catch (SQLException e) {
			CoreInterface.getSystem().handleException("Unable to get database connection.", e, NotificationStatus.BLOCK);
		}
		return l_retVal;
	}

	protected void initializeConnection() {

		ISqlConnection l_connection = new ISqlConnection() {
			
			@Override
			public void teardownConnection() {
			}
			
			@Override
			public void initializeConnection() {
			}
			
			@Override
			public String getUser() {
				return MockSqlConnection.this.getUser();
			}
			
			@Override
			public String getUrl() {
				return "jdbc:mysql://192.168.0.35/"+MockSqlConnection.this.getDataBase()+"?zeroDateTimeBehavior=convertToNull" +
						"&characterEncoding=utf-8&serverTimezone=UTC";
			}
			
			@Override
			public String getPassword() {
				return MockSqlConnection.this.getPassword();
			}
			
			@Override
			public int getMaxWait() {
				return 30;
			}
			
			@Override
			public int getMaxActive() {
				return 5;
			}

			@Override
			public String getJdbcDriver() {
				return "com.mysql.cj.jdbc.Driver";
			}
		};
		try {
			AirsPooledConnection.getInstance().initialize(l_connection);
		} catch (ClassNotFoundException e) {
			CoreInterface.getSystem().handleException("Unable to initialize database connection.", e, NotificationStatus.BLOCK);
		}
	}

	protected String getDataBase() {
		return "";
	}

	/**
	 * @return
	 */
	protected String getUser() {
		return "gotourn";
	}

	/**
	 * @return
	 */
	protected String getPassword() {
		return "gt139";
	}

}
