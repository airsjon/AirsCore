/**
 *
 */
package com.airsltd.core.lisp.function.exception;

/**
 * System unable to translate an argument to the needed Type.
 *
 * @author Jon Boley
 *
 */
public class InvalidArgument extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 8405290677187989908L;

	public InvalidArgument(String p_string) {
		super(p_string);
	}

}
