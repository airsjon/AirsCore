/**
 *
 */
package com.airsltd.core.lisp.function.comparison;

/**
 * Currently limited to numbers
 *
 * @author Jon Boley
 *
 */
public class GreaterThan extends CompareFunction {

	public GreaterThan() {
		super("GREATERTHAN");
	}

	@Override
	protected Integer processInteger(int p_oldValue, int p_newValue) {
		return p_oldValue <= p_newValue ? null : p_newValue;
	}

	@Override
	protected Float processFloat(float p_oldValue, float p_newValue) {
		return p_oldValue <= p_newValue ? null : p_newValue;
	}

	@Override
	public String getFunctionName() {
		return "Greater Than";
	}

	@Override
	protected Long processLong(long p_valueOne, long p_valueTwo) {
		return p_valueOne <= p_valueTwo ? null : p_valueTwo;
	}

	@Override
	protected Double processDouble(double p_valueOne, double p_valueTwo) {
		return p_valueOne <= p_valueTwo ? null : p_valueTwo;
	}

}
