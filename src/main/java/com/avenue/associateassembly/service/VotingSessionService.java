package com.avenue.associateassembly.service;

import java.util.List;

import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;

public interface VotingSessionService {

	VotingSessionResponseDto create(VotingSessionRequestDto dto);

	VotingSessionResponseDto findById(String id);

	List<VotingSessionResponseDto> findAll();

	VoteResponseDto addVote(String votingSessionId, VoteRequestDto dto);

	VotingSessionResultResponseDto getVotingSessionResult(String votingSessionId);
}
