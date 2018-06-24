/**
 * Copyright (c) 2011 Jon Boley, 432 NE Ravenna, Seattle Wa 98115.
 * All Rights Reserved.
 */
package com.airsltd.core;

/**
 * @author Jon
 *
 */
public interface SQLJSONProvider extends JSONProvider {

	void parseSqlData(Object sqlData);
}
