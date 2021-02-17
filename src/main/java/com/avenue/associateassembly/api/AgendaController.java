package com.avenue.associateassembly.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.service.AgendaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "Agenda")
@RequestMapping(value = "api/v1/agendas", produces = "application/json")
public class AgendaController {

	private final AgendaService agendaService;
	private final ModelMapper modelMapper;

    @Autowired
    public AgendaController(AgendaService agendaService, ModelMapper modelMapper) {
        this.agendaService = agendaService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value="Create one agenda", response = AgendaResponseDto.class)
    @ApiResponses(value = {@ApiResponse(code = 201, message = "Agenda successfully created.")})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<AgendaResponseDto> create(@RequestBody AgendaRequestDto agendaDto) throws URISyntaxException {
    	Agenda agenda = modelMapper.map(agendaDto, Agenda.class);
        Agenda agendaInDB = this.agendaService.create(agenda);
        
        AgendaResponseDto response = modelMapper.map(agendaInDB, AgendaResponseDto.class);
        return ResponseEntity.created(new URI(response.getId())).body(response);
    }
    
    @ApiOperation(value="Get one agenda by id", response = AgendaResponseDto.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agenda found.")})
    @GetMapping("/{id}")
    public ResponseEntity<AgendaResponseDto> findById(@PathVariable String id){
    	Agenda agenda = this.agendaService.findById(id);
        return ResponseEntity.ok(modelMapper.map(agenda, AgendaResponseDto.class));
    }
    
    @ApiOperation(value="List all agendas", response = AgendaResponseDto.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Agendas found.")})
    @GetMapping()
    public ResponseEntity<List<AgendaResponseDto>> listAll(){
    	List<Agenda> agendas = this.agendaService.findAll();
    	
    	List<AgendaResponseDto> response = agendas.stream().map(
    		agenda -> modelMapper.map(agenda, AgendaResponseDto.class)
    	).collect(Collectors.toList());
    	
        return ResponseEntity.ok(response);
    }
}
