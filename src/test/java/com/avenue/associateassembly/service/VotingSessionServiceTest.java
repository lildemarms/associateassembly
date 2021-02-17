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

import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Answer;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.exception.NotFoundException;
import com.avenue.associateassembly.exception.VotingSessionBlockedReadingResultsException;
import com.avenue.associateassembly.exception.VotingSessionExpiredException;
import com.avenue.associateassembly.exception.VotingSessionNotFoundException;
import com.avenue.associateassembly.integration.CpfService;
import com.avenue.associateassembly.repository.AgendaRepository;
import com.avenue.associateassembly.repository.VotingSessionRepository;

@RunWith(MockitoJUnitRunner.class)
public class VotingSessionServiceTest {

	@Mock
	public VotingSessionRepository votingSessionRepository;

	@Mock
	public AgendaRepository agendaRepository;
	
	@Mock
    public CpfService cpfService;

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
		ObjectId agendaId = new ObjectId();
		ObjectId votingSessionId = new ObjectId();
		Agenda agenda = new Agenda(agendaId, "Agenda 1");

		VotingSession votingSession = new VotingSession(agenda, 10);
		votingSession.setId(votingSessionId);

		Mockito.when(agendaRepository.findById(agendaId)).thenReturn(java.util.Optional.of(agenda));
		Mockito.when(votingSessionRepository.insert(new VotingSession(agenda, 10))).thenReturn(votingSession);

		VotingSession request = new VotingSession();
		request.setAgenda(new Agenda(agendaId, "Agenda 1"));
		request.setMinutesToExpiration(10);
		VotingSession response = votingSessionService.create(request);

		assertEquals(votingSessionId.toHexString(), response.getId().toHexString());
	}

	@Test(expected = NotFoundException.class)
	public void shouldNotCreateVotingSessionWhenAgendaNotExists() {
		ObjectId agendaId = new ObjectId();
		Agenda agenda = new Agenda(agendaId, "Agenda");
		
		VotingSession votingSession = new VotingSession(agenda, 10);
		
		votingSessionService.create(votingSession);
	}

	@Test
	public void shouldReturnVotingSessions() {
		List<VotingSession> votingSessions = new ArrayList<>();
		votingSessions.add(new VotingSession());
		votingSessions.add(new VotingSession());

		Mockito.when(votingSessionRepository.findAll()).thenReturn(votingSessions);

		List<VotingSession> response = votingSessionService.findAll();
		assertEquals(2, response.size());
	}

	@Test
	public void shouldVote() {
		ObjectId votingSessionId = new ObjectId();
		String cpf = "99142889014";

		Agenda agenda = new Agenda(new ObjectId(), "Agenda");
		VotingSession votingSession = new VotingSession(votingSessionId, agenda, 10);
		
		Mockito.when(votingSessionRepository.findById(votingSessionId)).thenReturn(java.util.Optional.of(votingSession));

		Vote dto = new Vote(cpf, Answer.NO);

		Mockito.when(cpfService.isAbleToVote(cpf)).thenReturn(true);
		
		boolean result = votingSessionService.addVote(votingSession.getId().toHexString(), dto);

		assertTrue(result);
	}

	@Test
	public void shouldReturnZeroVotingSessions() {
		List<VotingSession> response = votingSessionService.findAll();
		assertEquals(0, response.size());
	}

	@Test
	public void shouldReturnOneVotingSession() {
		ObjectId id = new ObjectId();
		VotingSession votingSession = new VotingSession();
		votingSession.setId(id);

		Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(votingSession));

		VotingSession response = votingSessionService.findById(id.toHexString());
		assertEquals(id.toHexString(), response.getId().toHexString());
	}

	@Test(expected = VotingSessionNotFoundException.class)
	public void shouldThrowNotFoundException() {
		ObjectId id = new ObjectId();
		Mockito.when(votingSessionRepository.findById(id)).thenThrow(new VotingSessionNotFoundException());

		votingSessionService.findById(id.toHexString());
	}

	@Test(expected = VotingSessionExpiredException.class)
	public void shouldThrowVotingSessionExpired() {
		ObjectId votingSessionId = new ObjectId();
		String cpf = "78773864005";

		Agenda agenda = new Agenda(new ObjectId(), "Agenda");

		VotingSession votingSession = new VotingSession(agenda, 1);
		votingSession.setId(votingSessionId);
		votingSession.setExpirationDate(votingSession.getExpirationDate().minusSeconds(61));

		Mockito.when(votingSessionRepository.findById(votingSessionId)).thenReturn(java.util.Optional.of(votingSession));
		Mockito.when(cpfService.isAbleToVote(cpf)).thenReturn(true);
		
		Vote vote = new Vote(cpf, Answer.NO);
		votingSessionService.addVote(votingSession.getId().toHexString(), vote);
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

        VotingSessionResultResponseDto response = votingSessionService.getVotingSessionResult(id.toHexString());

        assertEquals(2, response.getVoteCount().getNo());
        assertEquals(1, response.getVoteCount().getYes());
    }
	
	@Test(expected = VotingSessionBlockedReadingResultsException.class)
    public void shouldNotReturnResultWhenVotingSessionIsOpen(){
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("Agenda test - Should not return result when voting session is open");
        agenda.setId(id);

        VotingSession voting = new VotingSession(agenda, 10);
        voting.addVote(new Vote("1", Answer.NO));

        Mockito.when(votingSessionRepository.findById(id)).thenReturn(java.util.Optional.of(voting));

        votingSessionService.getVotingSessionResult(id.toHexString());
    }
}
