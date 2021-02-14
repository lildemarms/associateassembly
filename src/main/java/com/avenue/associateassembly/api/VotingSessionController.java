package com.avenue.associateassembly.api;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.service.VotingSessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Voting Session")
@RequestMapping(value = "api/v1/votingsessions", produces = "application/json")
public class VotingSessionController {

	private final VotingSessionService votingSessionService;

	@Autowired
	public VotingSessionController(VotingSessionService votingSessionService) {
		this.votingSessionService = votingSessionService;
	}

	@ApiOperation(value = "Create one voting session", response = VotingSessionResponseDto.class)
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public ResponseEntity<VotingSessionResponseDto> create(@RequestBody VotingSessionRequestDto votingSession) throws URISyntaxException {
		VotingSessionResponseDto response = this.votingSessionService.create(votingSession);
		return ResponseEntity.created(new URI(response.getId())).body(response);
	}
}