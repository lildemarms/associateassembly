package com.avenue.associateassembly.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Vote;
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

	@Override
	public VotingSessionResponseDto findById(String id) {
		VotingSession votingSession = this.votingSessionRepository.findById(new ObjectId(id))
				.orElseThrow(() -> new NotFoundException("Voting not found."));

		return modelMapper.map(votingSession, VotingSessionResponseDto.class);
	}

	@Override
	public List<VotingSessionResponseDto> findAll() {
		List<VotingSession> votingSessions = this.votingSessionRepository.findAll();

		return votingSessions.stream().map(vs -> modelMapper.map(vs, VotingSessionResponseDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public VoteResponseDto addVote(String votingSessionId, VoteRequestDto dto) {
		VotingSession votingSession = this.votingSessionRepository.findById(new ObjectId(votingSessionId))
				.orElseThrow(() -> new NotFoundException("Voting session not found."));

		// TODO: validar se votação já expirou
		// TODO: validar se cpf já votou/pode votar

		Vote vote = new Vote(dto.getCpf(), dto.getAnswer());
		votingSession.addVote(vote);
		votingSessionRepository.save(votingSession);

		VoteResponseDto resp = new VoteResponseDto();
		resp.setSuccess(true);
		return resp;
	}

}
