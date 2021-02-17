package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Answer;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VoteCount;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.service.VotingSessionService;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionControllerTest {

	@Mock
	public VotingSessionService votingSessionService;
	
	@Mock
	public ModelMapper modelMapper;

	@InjectMocks
	public VotingSessionController votingSessionController;

	 @Before
		public void setup() {
			ReflectionTestUtils.setField(votingSessionController, "modelMapper", new ModelMapper());
		}
	
	@Test
	public void shouldCreateVotingSession() throws URISyntaxException {
		ObjectId votingSessionId = new ObjectId();
		ObjectId agendaId = new ObjectId();
		
		VotingSession request = new VotingSession();
		request.setAgenda(new Agenda(agendaId, "Agenda"));
		
		VotingSession response = new VotingSession();
		response.setId(votingSessionId);
		
		Mockito.when(votingSessionService.create(request)).thenReturn(response);

		VotingSessionRequestDto requestDto = new VotingSessionRequestDto();
		requestDto.setAgendaId(agendaId.toHexString());
		
		ResponseEntity<VotingSessionResponseDto> responseEntity = votingSessionController.create(requestDto);

		assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void shouldReturnZeroVotingSession() {
		ResponseEntity<List<VotingSessionResponseDto>> resp = votingSessionController.listAll();

		assertEquals(resp.getStatusCode(), HttpStatus.OK);
		assertEquals(0, resp.getBody().size());
	}

	@Test
	public void shouldReturnVotingSessions() {
		List<VotingSession> vontingSessionFindAll = new ArrayList<>();
		VotingSession voting = new VotingSession();
		vontingSessionFindAll.add(voting);

		Mockito.when(votingSessionService.findAll()).thenReturn(vontingSessionFindAll);

		ResponseEntity<List<VotingSessionResponseDto>> responseEntity = votingSessionController.listAll();

		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		assertEquals(1, responseEntity.getBody().size());
	}

	@Test
	public void shouldVote() throws URISyntaxException {
		String votingSessionId = "602935bd572b6d0fe3e6c12e";
		Vote vote = new Vote("06993985098", Answer.YES);

		Mockito.when(votingSessionService.addVote(votingSessionId, vote)).thenReturn(true);

		VoteRequestDto voteDto = new VoteRequestDto();
		voteDto.setCpf("06993985098");
		voteDto.setAnswer(Answer.YES);
		
		ResponseEntity<VoteResponseDto> responseEntity = votingSessionController.vote(votingSessionId, voteDto);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
		assertNotNull(responseEntity.getBody());
	}

	@Test
	public void shouldGetVotingSessionResult() {
		String id = new ObjectId().toHexString();
		VotingSessionResultResponseDto response = new VotingSessionResultResponseDto();
		response.setAgenda(new AgendaResponseDto(id, "test"));
		response.setVoteCount(new VoteCount(1L, 2L));

		Mockito.when(votingSessionService.getVotingSessionResult(id)).thenReturn(response);

		ResponseEntity<?> responseEntity = votingSessionController.getVotingResult(id);
		assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
		assertNotNull(responseEntity.getBody());
	}
}
