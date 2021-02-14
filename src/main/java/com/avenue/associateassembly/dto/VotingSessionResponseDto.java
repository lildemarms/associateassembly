package com.avenue.associateassembly.dto;

import java.time.Instant;
import java.util.List;

import com.avenue.associateassembly.entity.Vote;

public class VotingSessionResponseDto {

	private String id;

	private AgendaResponseDto agenda;

	private Integer minutesToExpiration;

	private Instant expirationDate;

	private List<Vote> votes;

	private boolean closed;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AgendaResponseDto getAgenda() {
		return agenda;
	}

	public void setAgenda(AgendaResponseDto agenda) {
		this.agenda = agenda;
	}

	public Integer getMinutesToExpiration() {
		return minutesToExpiration;
	}

	public void setMinutesToExpiration(Integer minutesToExpiration) {
		this.minutesToExpiration = minutesToExpiration;
	}

	public Instant getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Instant expirationDate) {
		this.expirationDate = expirationDate;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
