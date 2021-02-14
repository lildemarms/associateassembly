package com.avenue.associateassembly.exception;

public class CPFAlreadyVotedException extends BusinessException {

	private static final long serialVersionUID = -1666529474784690749L;

	public CPFAlreadyVotedException(String cpf) {
		super("Associated with CPF (" + cpf + ") already voted.");
	}
}
