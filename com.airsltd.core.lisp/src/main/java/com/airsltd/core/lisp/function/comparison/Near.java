/**
 *
 */
package com.airsltd.core.lisp.function.comparison;

/**
 * @author Jon Boley
 *
 */
public class Near extends CompareFunction {

	public Near() {
		super("NEAR");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * processInteger(int, int)
	 */
	@Override
	protected Integer processInteger(int p_oldValue, int p_newValue) {
		return p_oldValue != p_newValue ? null : p_newValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * processFloat(float, float)
	 */
	@Override
	protected Float processFloat(float p_oldValue, float p_newValue) {
		return LispEquals.sameFloat(p_oldValue, p_newValue) ? p_oldValue : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Near";
	}

	@Override
	protected Long processLong(long p_valueOne, long p_valueTwo) {
		return p_valueOne != p_valueTwo ? null : p_valueTwo;
	}

	@Override
	protected Double processDouble(double p_valueOne, double p_valueTwo) {
		return LispEquals.sameDouble(p_valueOne, p_valueTwo) ? p_valueOne : p_valueTwo;
	}

}
