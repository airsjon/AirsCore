/**
 *
 */
package com.airsltd.core.lisp.function.comparison;

/**
 * @author jon_000
 *
 */
public class GreaterThanEqual extends CompareFunction {

	public GreaterThanEqual() {
		super("GREATERTHANEQUAL");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * processInteger(int, int)
	 */
	@Override
	protected Integer processInteger(int p_oldValue, int p_newValue) {
		return p_oldValue < p_newValue ? null : p_newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * processFloat(float, float)
	 */
	@Override
	protected Float processFloat(float p_oldValue, float p_newValue) {
		return p_oldValue < p_newValue ? null : p_newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Greater Than Equal";
	}

	@Override
	protected Long processLong(long p_valueOne, long p_valueTwo) {
		return p_valueOne < p_valueTwo ? null : p_valueTwo;
	}

	@Override
	protected Double processDouble(double p_valueOne, double p_valueTwo) {
		return p_valueOne < p_valueTwo ? null : p_valueTwo;
	}

}
