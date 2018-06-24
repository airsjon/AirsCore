/**
 *
 */
package com.airsltd.core.lisp.function;

/**
 * Convert to the {@code IType} of the implementing class.
 *
 * @author Jon Boley
 *
 */
public interface IType {

	/**
	 * Convert {@code p_value} into a new IType.
	 * <p>
	 * A contract to provide conversion of a {@link ISimpleValue} into another
	 * {@link ISimpleValue}. In most cases this conversion will be to a new type
	 * that the inheriting class represents.
	 *
	 * @param p_value
	 *            the value to be converted
	 * @param p_context
	 *            the context in which the conversion is to happen
	 * @return {@code p_value} converted into a new type
	 */
	ISimpleValue convert(ISimpleValue p_value, EvaluationContext p_context);

}
