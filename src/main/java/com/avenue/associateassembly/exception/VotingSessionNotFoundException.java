package com.avenue.associateassembly.exception;

public class VotingSessionNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 6239541040325637644L;

	public VotingSessionNotFoundException() {
		super("Voting session not found.");
	}
}
