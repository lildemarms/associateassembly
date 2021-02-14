package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.service.AgendaService;

@RunWith(MockitoJUnitRunner.class)
public class AgendaControllerTest {

	@Mock
    public AgendaService agendaService;

    @InjectMocks
    public AgendaController agendaController;
    
	@Test
    public void shouldCreateAgenda() throws URISyntaxException {
        String agendaName = "Agenda creation test";
        AgendaRequestDto request = new AgendaRequestDto(agendaName);
        AgendaResponseDto response = new AgendaResponseDto(new ObjectId().toHexString(), agendaName);
        Mockito.when(agendaService.createAgenda(request)).thenReturn(response);

        ResponseEntity<AgendaResponseDto> responseEntity = agendaController.create(request);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
    }
}
