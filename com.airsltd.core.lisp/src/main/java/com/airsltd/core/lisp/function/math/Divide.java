/**
 *
 */
package com.airsltd.core.lisp.function.math;

/**
 * @author jon_000
 *
 */
public class Divide extends MathFunction {

	/**
	 * @param p_functionName
	 */
	public Divide() {
		super("DIVIDE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.math.MathFunction#processInteger(
	 * int, int)
	 */
	@Override
	protected Integer processInteger(int p_value, int p_next) {
		return p_value / p_next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.math.MathFunction#processFloat(
	 * float, float)
	 */
	@Override
	protected Float processFloat(float p_value, float p_next) {
		return p_value / p_next;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.airsltd.aga.ranking.core.function.math.MathFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Divide";
	}

	@Override
	protected Double processDouble(double p_value, double p_next) {
		return p_value / p_next;
	}

	@Override
	protected Long processLong(long p_value, long p_next) {
		return p_value / p_next;
	}

}
