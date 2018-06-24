/**
 *
 */
package com.airsltd.core.lisp.function;

import com.airsltd.core.lisp.function.type.Type;

/**
 * @author Jon Boley
 *
 */
public interface ISimpleValue {

	Object getValue(EvaluationContext p_context);

	Type getType(EvaluationContext p_context);

	ISimpleValue evaluate(EvaluationContext p_context);

	<T> T as(Class<T> p_class, EvaluationContext p_context);

}
