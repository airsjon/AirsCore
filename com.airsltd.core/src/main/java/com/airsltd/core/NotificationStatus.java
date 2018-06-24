/**
 * Copyright (c) 2012, Jon Boley
 * 432 NE Ravenna Blvd
 * Seattle, WA 98115
 */
package com.airsltd.core;

/**
 * One-to-one with the StatusManager styles in
 * org.eclipse.ui.statushandlers.StatusManager
 * 
 * @author Jon Boley
 *
 */
public enum NotificationStatus {

	NONE(0x10), LOG(0x20), SHOW(0x40), BLOCK(0x80), TRACE(0x10), INFO(1), DEBUG(0x8), WARNING(0x2), ERROR(
			0x4), LOGTRACE(0x21), LOGDEBUG(0x28);

	public static final int ERRORBIT = 0x04;
	public static final int WARNINGBIT = 0x02;
	public static final int INFOBIT = 0x01;
	public static final int DEBUGBIT = 0x08;
	public static final int BLOCKBIT = 0x80;

	private int f_value;

	private NotificationStatus(int p_value) {
		f_value = p_value;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return f_value;
	}

}
