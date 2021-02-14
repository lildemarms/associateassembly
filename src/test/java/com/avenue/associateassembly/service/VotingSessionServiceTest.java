package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
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
import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Answer;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.exception.BusinessException;
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
	public void shouldCreateVotingSession() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should create voting session");

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
	public void shouldNotCreateVotingSessionWhenAgendaNotExists() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should not create voting session when agenda not exists");

		VotingSession votingSession = new VotingSession(agenda, 10);
		votingSession.setId(id);

		VotingSessionRequestDto request = new VotingSessionRequestDto();
		request.setAgendaId(votingSession.getId().toHexString());

		votingSessionService.create(request);
	}

	@Test
	public void shouldReturnVotingSessions() {
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

	@Test
	public void shouldReturnZeroVotingSessions() {
		List<VotingSessionResponseDto> resp = votingSessionService.findAll();
		assertEquals(0, resp.size());
	}

	@Test
	public void shouldReturnOneVotingSession() {
		ObjectId id = new ObjectId();
		VotingSession votingSession = new VotingSession();
		votingSession.setId(id);

		Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(votingSession));

		VotingSessionResponseDto resp = votingSessionService.findById(id.toHexString());
		assertEquals(id.toHexString(), resp.getId());
	}

	@Test(expected = NotFoundException.class)
	public void shouldThrowNotFoundException() {
		ObjectId id = new ObjectId();
		Mockito.when(votingSessionRepository.findById(id)).thenThrow(new NotFoundException("Voting not found."));

		votingSessionService.findById(id.toHexString());
	}

	@Test(expected = BusinessException.class)
	public void shouldThrowVotingSessionExpired() {
		ObjectId id = new ObjectId();
		Agenda agenda = new Agenda("Agenda test - Should throw voting session expired");
		VotingSession votingSession = new VotingSession(agenda, 1);

		votingSession.setId(id);
		votingSession.setExpirationDate(votingSession.getExpirationDate().minusSeconds(61));

		Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(votingSession));

		VoteRequestDto dto = new VoteRequestDto();
		dto.setCpf("78773864005");
		dto.setAnswer(Answer.NO);
		votingSessionService.addVote(votingSession.getId().toHexString(), dto);
	}
	
	@Test
    public void shouldReturnVotingSessionResult(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("Agenda test - Should return voting session result");
        agenda.setId(id);

        VotingSession votingSession = new VotingSession(agenda, 1);
        votingSession.addVote(new Vote("1", Answer.NO));
        votingSession.addVote(new Vote("2", Answer.NO));
        votingSession.addVote(new Vote("3", Answer.YES));
        votingSession.setExpirationDate(Instant.now().minusSeconds(10));

        Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(votingSession));

        VotingSessionResultResponseDto resp = votingSessionService.getVotingSessionResult(id.toHexString());

        assertEquals(2, resp.getVoteCount().getNo());
        assertEquals(1, resp.getVoteCount().getYes());
    }
}
