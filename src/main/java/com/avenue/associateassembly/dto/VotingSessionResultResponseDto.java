package com.avenue.associateassembly.dto;

import com.avenue.associateassembly.entity.VoteCount;

public class VotingSessionResultResponseDto {

	private AgendaResponseDto agenda;
    private VoteCount voteCount;

    public AgendaResponseDto getAgenda() {
        return agenda;
    }

    public void setAgenda(AgendaResponseDto agenda) {
        this.agenda = agenda;
    }

    public VoteCount getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(VoteCount voteCount) {
        this.voteCount = voteCount;
    }
}
