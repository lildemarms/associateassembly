package com.avenue.associateassembly.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionTest {

	@Test
	public void shouldReturnTrueIfExpired() {
		VotingSession votingSession = new VotingSession();
		votingSession.setExpirationDate(Instant.now().minusSeconds(30));

		assertTrue(votingSession.isExpired());
	}

	@Test
	public void shouldReturnFalseIfNotExpired() {
		VotingSession votingSession = new VotingSession();
		votingSession.setExpirationDate(Instant.now().plusSeconds(30));

		assertFalse(votingSession.isExpired());
	}

	@Test
	public void shouldValidateCpfAlreadyVoted() {
		VotingSession votingSession = new VotingSession();

		votingSession.addVote(new Vote("20127587039", Answer.NO));
		votingSession.addVote(new Vote("93204718000", Answer.YES));
		votingSession.addVote(new Vote("01214941010", Answer.YES));

		assertTrue(votingSession.cpfAlreadyVoted("20127587039"));
	}
}
