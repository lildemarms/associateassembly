package com.avenue.associateassembly.dto;

public class VotingSessionRequestDto {

	private String agendaId;

	private Integer minutesToExpiration;

	public String getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(String agendaId) {
		this.agendaId = agendaId;
	}

	public Integer getMinutesToExpiration() {
		return minutesToExpiration;
	}

	public void setMinutesToExpiration(Integer minutesToExpiration) {
		this.minutesToExpiration = minutesToExpiration;
	}
}
