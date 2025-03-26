package com.ninjaone.dundie_awards.exception;

public class OrganizationNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public OrganizationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public OrganizationNotFoundException(String message) {
		super(message);
	}
	
	public OrganizationNotFoundException(Throwable cause) {
		super(cause);
	}
}
