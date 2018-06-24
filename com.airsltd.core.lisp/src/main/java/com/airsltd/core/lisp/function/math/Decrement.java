/**
 *
 */
package com.airsltd.core.lisp.function.math;

/**
 * @author jon_000
 *
 */
public class Decrement extends IntegerSingleFunction {

	/**
	 *
	 */
	public Decrement() {
		super("DEC");
	}

	@Override
	protected int modifyInteger(int p_value) {
		return p_value - 1;
	}

	@Override
	public String getFunctionName() {
		return "Decrement";
	}

}
