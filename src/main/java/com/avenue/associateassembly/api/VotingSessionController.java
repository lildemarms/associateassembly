package com.avenue.associateassembly.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avenue.associateassembly.dto.VoteRequestDto;
import com.avenue.associateassembly.dto.VoteResponseDto;
import com.avenue.associateassembly.dto.VotingSessionRequestDto;
import com.avenue.associateassembly.dto.VotingSessionResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.entity.Vote;
import com.avenue.associateassembly.entity.VotingSession;
import com.avenue.associateassembly.service.VotingSessionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Voting Session")
@RequestMapping(value = "api/v1/votingsessions", produces = "application/json")
public class VotingSessionController {

	private final VotingSessionService votingSessionService;
	private final ModelMapper modelMapper;

	@Autowired
	public VotingSessionController(VotingSessionService votingSessionService, ModelMapper modelMapper) {
		this.votingSessionService = votingSessionService;
		this.modelMapper = modelMapper;
	}

	@ApiOperation(value = "Create one voting session", response = VotingSessionResponseDto.class)
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Voting successfully created.")})
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping()
	public ResponseEntity<VotingSessionResponseDto> create(@RequestBody VotingSessionRequestDto dto)
			throws URISyntaxException {
		VotingSession votingSession = votingSessionService.create(convertRequestToVotingSession(dto));
		VotingSessionResponseDto response = convertToResponseDto(votingSession);
		return ResponseEntity.created(new URI(response.getId())).body(response);
	}

	@ApiOperation(value = "List all voting sessions", response = VotingSessionResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Voting sessions found.") })
	@GetMapping()
	public ResponseEntity<List<VotingSessionResponseDto>> listAll() {
		List<VotingSession> votingSessions = this.votingSessionService.findAll();

		List<VotingSessionResponseDto> response = votingSessions.stream()
    			.map(this::convertToResponseDto)
    			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "Find one voting session by id", response = VotingSessionResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Voting session found.") })
	@GetMapping("/{id}")
	public ResponseEntity<VotingSessionResponseDto> findById(@PathVariable String id) {
		VotingSession votingSession = votingSessionService.findById(id);
		return ResponseEntity.ok(convertToResponseDto(votingSession));
	}
	
	@ApiOperation(value="Add a vote in a voting session", response = VoteResponseDto.class)
	@ApiResponses(value = {@ApiResponse(code = 201, message = "Vote successfully added.")})
    @PutMapping("/{votingSessionId}/vote")
    public ResponseEntity<VoteResponseDto> vote(@PathVariable String votingSessionId, @RequestBody VoteRequestDto dto) throws URISyntaxException{
		Vote vote = new Vote(dto.getCpf(), dto.getAnswer());

        VoteResponseDto response = new VoteResponseDto();
		response.setSuccess(votingSessionService.addVote(votingSessionId, vote));
        
        return ResponseEntity.created(new URI(response.toString())).body(response);
    }
	
	@ApiOperation(value="Get the voting session result", response = VotingSessionResponseDto.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Voting session result found.")})
    @GetMapping("/{votingSessionId}/result")
    public ResponseEntity<?> getVotingResult(@PathVariable String votingSessionId){
        return ResponseEntity.ok(this.votingSessionService.getVotingSessionResult(votingSessionId));
    }
	
	private VotingSessionResponseDto convertToResponseDto(VotingSession post) {
    	return modelMapper.map(post, VotingSessionResponseDto.class);
    }
    
    private VotingSession convertRequestToVotingSession(VotingSessionRequestDto dto) {
    	Agenda agenda = new Agenda();
    	agenda.setId(new ObjectId(dto.getAgendaId()));

    	VotingSession votingSession = new VotingSession();
    	votingSession.setMinutesToExpiration(dto.getMinutesToExpiration());
    	votingSession.setAgenda(agenda);    	
    	return votingSession;
    }
}
