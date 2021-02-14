package com.avenue.associateassembly.service;

import java.util.Objects;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.exception.NotFoundException;
import com.avenue.associateassembly.repository.AgendaRepository;
import com.avenue.associateassembly.repository.VotingSessionRepository;

@Service
public class VotingSessionServiceImpl implements VotingSessionService {

	private static final String DEFAULT_EXPIRATION_MINUTES = "default.expiration.minutes";

	private final VotingSessionRepository votingSessionRepository;
	private final AgendaRepository agendaRepository;
	private final ModelMapper modelMapper;
	private final Environment environment;

	@Autowired
	public VotingSessionServiceImpl(VotingSessionRepository votingSessionRepository, ModelMapper modelMapper,
			AgendaRepository agendaRepository, Environment environment) {

		this.votingSessionRepository = votingSessionRepository;
		this.agendaRepository = agendaRepository;
		this.modelMapper = modelMapper;
		this.environment = environment;
	}

	@Override
	public VotingSessionResponseDto create(VotingSessionRequestDto dto) {
		Agenda agenda = this.agendaRepository.findById(new ObjectId(dto.getAgendaId()))
				.orElseThrow(() -> new NotFoundException("Agenda not found."));

		if (dto.getMinutesToExpiration() == null) {
			dto.setMinutesToExpiration(getDefaultExpirationMinutes());
		}

		VotingSession votingSession = new VotingSession(agenda, dto.getMinutesToExpiration());
		votingSession = this.votingSessionRepository.insert(votingSession);

		return modelMapper.map(votingSession, VotingSessionResponseDto.class);
	}

	private int getDefaultExpirationMinutes() {
		return Integer.parseInt(Objects.requireNonNull(environment.getProperty(DEFAULT_EXPIRATION_MINUTES)));
	}

}
