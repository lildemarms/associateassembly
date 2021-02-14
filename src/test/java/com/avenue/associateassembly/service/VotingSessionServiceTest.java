package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.entity.Agenda;
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
    public VotingSessionServiceImpl votingService;

    @Before
    public void setup(){
        ReflectionTestUtils.setField(votingService, "modelMapper", new ModelMapper());
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
        VotingSessionResponseDto response = votingService.create(request);

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

        votingService.create(request);
    }

}
