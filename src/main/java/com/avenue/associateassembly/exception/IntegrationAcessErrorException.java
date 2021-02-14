package com.avenue.associateassembly.exception;

public class IntegrationAcessErrorException extends IntegrationException {

	private static final long serialVersionUID = -2184129549067300525L;

	public IntegrationAcessErrorException() {
		super("An error occurred while trying to access 'CpfService'.");
	}
}
