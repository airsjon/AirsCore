/**
 *
 */
package com.airsltd.core.data;

import java.util.Date;

/**
 * This simple tuple represents a range of dates. For single day ranges, the
 * second value of the tuple can be null. The implementor can decide if the
 * range is inclusive or exclusive.
 *
 * @author Jon Boley
 *
 */
public class DateRange {

	/**
	 * The first date.
	 */
	private Date f_start;
	/**
	 * The second date. This value can be <code>null</code>. In this case, the
	 * range is represented by the first date.
	 */
	private Date f_end;

	/**
	 * the Range consists of a single date
	 *
	 * @param start
	 */
	public DateRange(Date start) {
		super();
		setStart(start);
		setEnd(null);
	}

	/**
	 * the Range consists of two dates. Both dates are inclusive.
	 *
	 * @param start
	 * @param end
	 */
	public DateRange(Date p_start, Date p_end) {
		super();
		setStart(p_start);
		setEnd(p_end);
	}

	/**
	 * @return the start
	 */
	public Date getStart() {
		return f_start;
	}

	/**
	 * @param start
	 *            the start to set
	 */
	public void setStart(Date p_start) {
		f_start = p_start;
	}

	/**
	 * @return the end
	 */
	public Date getEnd() {
		return f_end == null ? f_start : f_end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(Date p_end) {
		f_end = f_start.equals(p_end) ? null : p_end;
	}

}
