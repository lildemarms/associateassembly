package com.avenue.associateassembly.exception;

public class VotingSessionExpiredException extends BusinessException {

	private static final long serialVersionUID = -1666529474784690749L;

	public VotingSessionExpiredException() {
		super("Voting session already expired.");
	}
}
