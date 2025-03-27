package com.ninjaone.dundie_awards.exception;

/**
 * Unexpected exception
 */
public class UnexpectedException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnexpectedException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnexpectedException(String message) {
		super(message);
	}
	
	public UnexpectedException(Throwable cause) {
		super(cause);
	}
}
