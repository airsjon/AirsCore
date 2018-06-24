/**
 *
 */
package com.airsltd.core.lisp.function.math;

/**
 * @author jon_000
 *
 */
public class Increment extends IntegerSingleFunction {

	/**
	 * @param p_functionName
	 */
	public Increment() {
		super("INC");
	}

	@Override
	protected int modifyInteger(int p_value) {
		return p_value + 1;
	}

	@Override
	public String getFunctionName() {
		return "Increment";
	}

}
