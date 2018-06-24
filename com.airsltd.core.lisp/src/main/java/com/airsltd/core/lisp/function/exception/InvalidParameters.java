/**
 *
 */
package com.airsltd.core.lisp.function.exception;

/**
 * The argument list passed to the function is not well-formed (to-few, to-many,
 * etc)
 *
 * @author Jon Boley
 *
 */
public class InvalidParameters extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 765902517942470010L;

	public InvalidParameters(String p_string) {
		super(p_string);
	}

}
