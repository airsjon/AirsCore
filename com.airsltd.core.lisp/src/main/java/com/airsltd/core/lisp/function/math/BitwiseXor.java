/**
 *
 */
package com.airsltd.core.lisp.function.math;

/**
 * @author jon_000
 *
 */
public class BitwiseXor extends IntegerMultipleFunction {

	/**
	 * @param p_functionName
	 */
	public BitwiseXor() {
		super("BITWISEXOR");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.math.IntegerMultipleFunction#
	 * modifyInteger(int, int)
	 */
	@Override
	protected int modifyInteger(int p_value, int p_nextValue) {
		return p_value ^ p_nextValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.math.IntegerMultipleFunction#
	 * getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Bitwise Xor";
	}

}
