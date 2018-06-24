/**
 *
 */
package com.airsltd.core.lisp.function;

import java.util.List;

/**
 * @author jon_000
 *
 */
public interface IValue extends ISimpleValue {

	int dimensions();

	List<Integer> span();

	boolean validIndex(List<Integer> p_index);

	Object getValue(List<Integer> p_index);

	IType getType(List<Integer> p_index);

}
