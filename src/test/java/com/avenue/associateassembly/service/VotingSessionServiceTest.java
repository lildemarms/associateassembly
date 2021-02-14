package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.test.util.ReflectionTestUtils;

import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Answer;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.exception.NotFoundException;
import com.avenue.associateassembly.repository.AgendaRepository;
import com.avenue.associateassembly.repository.VotingSessionRepository;


@RunWith(MockitoJUnitRunner.class)
public class VotingSessionServiceTest {

	@Mock
	public VotingSessionRepository votingSessionRepository;

	@Mock
	public AgendaRepository agendaRepository;

	@Mock
	public ModelMapper modelMapper;

	@InjectMocks
	public VotingSessionServiceImpl votingSessionService;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(votingSessionService, "modelMapper", new ModelMapper());
	}

	@Test
	public void shouldCreateVoting() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should create voting");

		VotingSession votingSession = new VotingSession(agenda, 10);
		votingSession.setId(id);

		Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));
		Mockito.when(votingSessionRepository.insert(new VotingSession(agenda, 10))).thenReturn(votingSession);

		VotingSessionRequestDto request = new VotingSessionRequestDto();
		request.setAgendaId(votingSession.getId().toHexString());
		request.setMinutesToExpiration(10);
		VotingSessionResponseDto response = votingSessionService.create(request);

		assertEquals(id.toHexString(), response.getId());
	}

	@Test(expected = NotFoundException.class)
	public void shouldNotCreateVotingWhenAgendaNotExists() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should not create voting when agenda not exists");

		VotingSession votingSession = new VotingSession(agenda, 10);
		votingSession.setId(id);

		VotingSessionRequestDto request = new VotingSessionRequestDto();
		request.setAgendaId(votingSession.getId().toHexString());

		votingSessionService.create(request);
	}

	@Test
	public void shouldReturnVotings() {
		List<VotingSession> votingSessions = new ArrayList<>();
		votingSessions.add(new VotingSession());
		votingSessions.add(new VotingSession());

		Mockito.when(votingSessionRepository.findAll()).thenReturn(votingSessions);

		List<VotingSessionResponseDto> resp = votingSessionService.findAll();
		assertEquals(2, resp.size());
	}

	@Test
	public void shouldVote() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should vote");

		VotingSession votingSession = new VotingSession(agenda, 10);
		Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(votingSession));

		VotingSession votingSession2 = new VotingSession(agenda, 10);
		votingSession2.setId(id);

		Vote vote = new Vote("99142889014", Answer.NO);
		votingSession2.addVote(vote);

		Mockito.when(votingSessionRepository.save(votingSession)).thenReturn(votingSession2);

		VoteRequestDto dto = new VoteRequestDto();
		dto.setCpf("99142889014");
		dto.setAnswer(Answer.NO);

		VoteResponseDto voteResponse = votingSessionService.addVote(votingSession2.getId().toHexString(), dto);

		assertTrue(voteResponse.isSuccess());
	}

}
