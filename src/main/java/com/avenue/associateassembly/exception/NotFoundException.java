package com.avenue.associateassembly.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5943161612546978053L;

	public NotFoundException(String message) {
		super(message);
	}
}
