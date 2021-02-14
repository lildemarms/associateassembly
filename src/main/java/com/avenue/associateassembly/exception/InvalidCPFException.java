package com.avenue.associateassembly.exception;

public class InvalidCPFException extends BusinessException {

	private static final long serialVersionUID = -5890298234574370421L;

	public InvalidCPFException() {
		super("Invalid CPF!");
	}
}
