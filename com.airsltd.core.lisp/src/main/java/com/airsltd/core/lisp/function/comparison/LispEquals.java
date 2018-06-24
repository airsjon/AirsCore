/**
 *
 */
package com.airsltd.core.lisp.function.comparison;

/**
 * @author jon_000
 *
 */
public class LispEquals extends CompareFunction {

	private static final float FLOATNEAR = 0.00001F;
	private static final double DOUBLENEAR = 0.0000001D;

	public LispEquals() {
		super("EQUALS");
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
		return sameFloat(p_oldValue, p_newValue) ? p_oldValue : null;
	}

	protected static boolean sameFloat(float p_oldValue, float p_newValue) {
		return Math.abs(p_oldValue - p_newValue) <= FLOATNEAR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.airsltd.aga.ranking.core.function.comparison.CompareFunction#
	 * getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		return "Equals";
	}

	public static boolean sameDouble(double p_valueOne, double p_valueTwo) {
		return Math.abs(p_valueOne - p_valueTwo) <= DOUBLENEAR;
	}

	@Override
	protected Long processLong(long p_valueOne, long p_valueTwo) {
		return p_valueOne != p_valueTwo ? null : p_valueTwo;
	}

	@Override
	protected Double processDouble(double p_valueOne, double p_valueTwo) {
		return sameDouble(p_valueOne, p_valueTwo) ? p_valueOne : null;
	}

}
