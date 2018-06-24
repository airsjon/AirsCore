/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core.data;

/**
 * Interface to define a connection by the Airs Database system to a valid SQL
 * system.
 *
 * @author Jon Boley
 *
 */
public interface ISqlConnection {

	/**
	 * URL of the SQL System to connect to
	 *
	 * @return
	 */
	String getUrl();

	/**
	 * The User to log onto the system with
	 * 
	 * @return
	 */
	String getUser();

	/**
	 * The Password to use when logging onto the system
	 * 
	 * @return
	 */
	String getPassword();

	/**
	 * Return the maximum active connections to be allowed
	 * 
	 * @return
	 */
	int getMaxActive();

	/**
	 * Return the time to wait before initiating a Keep Alive ping
	 * 
	 * @return
	 */
	int getMaxWait();

	/**
	 * This hook method is called after the connection is create to allow for
	 * specific initialization to take place. (For example, during a test
	 * database connection the mock database can be loaded.)
	 */
	void initializeConnection();

	/**
	 * This hook method is called when the connection is closed and provides a
	 * means for the interface to handle any clean up tasks.
	 */
	void teardownConnection();

	/**
	 * Return the driver to use for JDBC connections.
	 * 
	 * @return
	 */
	String getJdbcDriver();

}
