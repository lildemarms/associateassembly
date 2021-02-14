package com.avenue.associateassembly.exception;

public class CPFUnableVoteException extends BusinessException {

	private static final long serialVersionUID = -3776549094504019649L;

	public CPFUnableVoteException() {
		super("CPF is unable to vote.");
	}
}
