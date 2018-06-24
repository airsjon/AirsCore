/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class AirsPooledConnection {

	private static final int MINIMUMIDLE = 2;
	private static final int MAXIMUMIDLE = 5;
	private static final int INITIALSIZE = 2;
	private static final int EVICITIONRUN = 30000;
	private static AirsPooledConnection INSTANCE = null;
	private DataSource f_datasource;

	private AirsPooledConnection() {
		INSTANCE = this;
	}

	public static AirsPooledConnection getInstance() {
		if (INSTANCE == null) {
			new AirsPooledConnection();
		}
		return INSTANCE;
	}

	public static void setInstance(AirsPooledConnection p_connection) {
		INSTANCE = p_connection;
	}

	public void initialize(ISqlConnection connData) throws ClassNotFoundException {
		f_datasource = new DataSource();
		f_datasource.setPoolProperties(createPool(connData));
	}

	private PoolConfiguration createPool(ISqlConnection p_connData) {
		final PoolProperties l_retVal = new PoolProperties();
		
		l_retVal.setDriverClassName(p_connData.getJdbcDriver());
		l_retVal.setUrl(p_connData.getUrl());
		l_retVal.setUsername(p_connData.getUser());
		l_retVal.setPassword(p_connData.getPassword());

		l_retVal.setMaxWait(p_connData.getMaxWait());
		l_retVal.setInitialSize(INITIALSIZE);
		l_retVal.setMaxActive(p_connData.getMaxActive());
		l_retVal.setMaxIdle(MAXIMUMIDLE);
		l_retVal.setMinIdle(MINIMUMIDLE);
		l_retVal.setTestWhileIdle(true);
		l_retVal.setTimeBetweenEvictionRunsMillis(EVICITIONRUN);
		l_retVal.setValidationQuery("select 1");
		return l_retVal;
	}

	/**
	 * @return the datasource
	 */
	public DataSource getDatasource() {
		return f_datasource;
	}

	/**
	 * @param p_datasource
	 *            the datasource to set
	 */
	public void setDatasource(DataSource p_datasource) {
		f_datasource = p_datasource;
	}

	public Connection getConnection() throws SQLException {
		if (f_datasource != null) {
			return f_datasource.getConnection();
		} else {
			throw new AirsConnectionException("Connection interface not setup");
		}
	}

}
