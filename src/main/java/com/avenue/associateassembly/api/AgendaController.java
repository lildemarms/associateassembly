package com.avenue.associateassembly.api;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.service.AgendaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "agendas")
@RequestMapping(value = "api/v1/agendas", produces = "application/json")
public class AgendaController {

	private final AgendaService agendaService;

    @Autowired
    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @ApiOperation(value="Create one agenda", response = AgendaResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Agenda successfully created.")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public ResponseEntity<AgendaResponseDto> create(@RequestBody AgendaRequestDto agenda) throws URISyntaxException {
        AgendaResponseDto response = this.agendaService.createAgenda(agenda);
        return ResponseEntity.created(new URI(response.getId())).body(response);
    }
}
