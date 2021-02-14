package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.service.VotingSessionService;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionControllerTest {

	@Mock
	public VotingSessionService votingSessionService;

	@InjectMocks
	public VotingSessionController votingSessionController;

	@Test
	public void shouldCreateVoting() throws URISyntaxException {
		VotingSessionRequestDto request = new VotingSessionRequestDto();
		VotingSessionResponseDto response = new VotingSessionResponseDto();

		response.setId(new ObjectId().toHexString());
		Mockito.when(votingSessionService.create(request)).thenReturn(response);

		ResponseEntity<VotingSessionResponseDto> responseEntity = votingSessionController.create(request);

		assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void shouldReturnZeroVotings() {
		ResponseEntity<List<VotingSessionResponseDto>> resp = votingSessionController.listAll();

		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(0, ((LinkedList<VotingSessionResponseDto>) resp.getBody()).size());
	}

	@Test
	public void shouldReturnVotings() {
		List<VotingSessionResponseDto> listDto = new ArrayList<>();
		VotingSessionResponseDto voting = new VotingSessionResponseDto();
		listDto.add(voting);

		Mockito.when(votingSessionService.findAll()).thenReturn(listDto);

		ResponseEntity<List<VotingSessionResponseDto>> responseEntity = votingSessionController.listAll();

		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		assertEquals(1, ((ArrayList<VotingSessionResponseDto>) responseEntity.getBody()).size());
	}

	@Test
	public void shouldVote() throws URISyntaxException {
		String votingSessionId = "602935bd572b6d0fe3e6c12e";
		VoteRequestDto request = new VoteRequestDto();
		VoteResponseDto response = new VoteResponseDto(true);

		Mockito.when(votingSessionService.addVote(votingSessionId, request)).thenReturn(response);

		ResponseEntity<VoteResponseDto> responseEntity = votingSessionController.vote(votingSessionId, request);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(responseEntity.getBody());
	}
}
