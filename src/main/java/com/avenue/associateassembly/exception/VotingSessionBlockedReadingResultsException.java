package com.avenue.associateassembly.exception;

import java.time.Instant;

public class VotingSessionBlockedReadingResultsException extends BusinessException {

	private static final long serialVersionUID = -1666529474784690749L;

	public VotingSessionBlockedReadingResultsException(Instant expirationDate) {
		super("Voting session still open and blocked for reading the results. This session will end on " + expirationDate.toString());
	}
}
