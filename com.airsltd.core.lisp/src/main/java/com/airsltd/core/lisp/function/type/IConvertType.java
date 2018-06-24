/**
 *
 */
package com.airsltd.core.lisp.function.type;

import com.airsltd.core.lisp.function.EvaluationContext;
import com.airsltd.core.lisp.function.ISimpleValue;

/**
 * A method for converting from one type to another
 *
 * @author Jon Boley
 *
 */
public interface IConvertType {

	/**
	 * Convert {@code p_value} into a new Type.
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
