package com.avenue.associateassembly.service;

import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;

public interface VotingSessionService {
	
	VotingSessionResponseDto create(VotingSessionRequestDto dto);
}
