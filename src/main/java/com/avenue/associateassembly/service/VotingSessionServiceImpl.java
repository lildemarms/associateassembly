package com.avenue.associateassembly.service;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.dto.VotingSessionResultResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Answer;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VoteCount;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.exception.AgendaNotFoundException;
import com.avenue.associateassembly.exception.CPFAlreadyVotedException;
import com.avenue.associateassembly.exception.CPFUnableVoteException;
import com.avenue.associateassembly.exception.VotingSessionBlockedReadingResultsException;
import com.avenue.associateassembly.exception.VotingSessionExpiredException;
import com.avenue.associateassembly.exception.VotingSessionNotFoundException;
import com.avenue.associateassembly.integration.CpfService;
import com.avenue.associateassembly.repository.AgendaRepository;
import com.avenue.associateassembly.repository.VotingSessionRepository;

@Service
public class VotingSessionServiceImpl implements VotingSessionService {

	Logger logger = LoggerFactory.getLogger(VotingSessionServiceImpl.class);
	
	private static final String DEFAULT_EXPIRATION_MINUTES = "default.expiration.minutes";

	private final VotingSessionRepository votingSessionRepository;
	private final AgendaRepository agendaRepository;
	private final ModelMapper modelMapper;
	private final Environment environment;
	private final CpfService cpfService;

	@Autowired
	public VotingSessionServiceImpl(VotingSessionRepository votingSessionRepository, ModelMapper modelMapper,
			AgendaRepository agendaRepository, Environment environment, CpfService cpfService) {

		this.votingSessionRepository = votingSessionRepository;
		this.agendaRepository = agendaRepository;
		this.modelMapper = modelMapper;
		this.environment = environment;
		this.cpfService = cpfService;
	}

	@Override
	public VotingSession create(VotingSession votingSessionIn) {
		Agenda agendaToFind = votingSessionIn.getAgenda();
		
		Agenda agendaInDb = this.agendaRepository.findById(agendaToFind.getId())
				.orElseThrow(() -> agendaNotFound(agendaToFind));

		Integer minutesToExpiration = getMinutesToExpiration(votingSessionIn);

		VotingSession votingSessionToSave = new VotingSession(agendaInDb, minutesToExpiration);
		return this.votingSessionRepository.insert(votingSessionToSave);
	}

	private AgendaNotFoundException agendaNotFound(Agenda dto) {
		logger.info("Agenda (id: {}) not found.", dto.getId().toHexString());
		return new AgendaNotFoundException();
	}
	
	private Integer getMinutesToExpiration(VotingSession dto) {
		Integer minutesToExpiration = dto.getMinutesToExpiration();

		if (minutesToExpiration == null || minutesToExpiration <= 0) {
			return getDefaultExpirationMinutes();
		}
		
		return minutesToExpiration;
	}

	private int getDefaultExpirationMinutes() {
		return Integer.parseInt(Objects.requireNonNull(environment.getProperty(DEFAULT_EXPIRATION_MINUTES)));
	}

	@Override
	public VotingSession findById(String votingSessionId) {
		return this.votingSessionRepository.findById(new ObjectId(votingSessionId))
				.orElseThrow(() -> votingSessionNotFound(votingSessionId));
	}

	@Override
	public List<VotingSession> findAll() {
		return this.votingSessionRepository.findAll();
	}

	@Override
	public boolean addVote(String votingSessionId, Vote vote) {
		if (!cpfService.isAbleToVote(vote.getCpf())) {
			logger.info("CPF is unable to vote.");
            throw new CPFUnableVoteException();
		}
		
		VotingSession votingSession = findById(votingSessionId);
		
		if (votingSession.isExpired()) {
			logger.info("Voting already expired.");
			throw new VotingSessionExpiredException();
		}
		
		if (votingSession.cpfAlreadyVoted(vote.getCpf())) {
			logger.info("Associated with CPF ({}) already voted.", vote.getCpf());
			throw new CPFAlreadyVotedException(vote.getCpf());
		}

		votingSession.addVote(vote);
		votingSessionRepository.save(votingSession);

		return true;
	}

	@Override
	public VotingSessionResultResponseDto getVotingSessionResult(String votingSessionId) {
		VotingSession votingSession = findById(votingSessionId);

		if (!votingSession.isExpired()) {
			logger.info("Voting session still open and blocked for reading the results. This session will end on {}", votingSession.getExpirationDate().toString());
            throw new VotingSessionBlockedReadingResultsException(votingSession.getExpirationDate());
		}
		
		List<Vote> votes = votingSession.getVotes();
		
		VoteCount voteCount = new VoteCount(
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.YES)).count(),
                votes.stream().filter(vote -> vote.getAnswer().equals(Answer.NO)).count()
        );
		
		VotingSessionResultResponseDto resultResponseDto = new VotingSessionResultResponseDto();
        resultResponseDto.setAgenda(modelMapper.map(votingSession.getAgenda(), AgendaResponseDto.class));
        resultResponseDto.setVoteCount(voteCount);

        return resultResponseDto;
	}

	private VotingSessionNotFoundException votingSessionNotFound(String votingSessionId) {
		logger.info("Voting session (id: {}) not found.", votingSessionId);
		return new VotingSessionNotFoundException();
	}

}
