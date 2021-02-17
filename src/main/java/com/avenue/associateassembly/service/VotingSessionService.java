package com.avenue.associateassembly.service;

import java.util.List;

import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VotingSession;

public interface VotingSessionService {

	VotingSession create(VotingSession dto);

	VotingSession findById(String id);

	List<VotingSession> findAll();

	boolean addVote(String votingSessionId, Vote dto);

	VotingSessionResultResponseDto getVotingSessionResult(String votingSessionId);
}
