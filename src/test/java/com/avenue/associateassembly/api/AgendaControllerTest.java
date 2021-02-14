package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        Mockito.when(agendaService.create(request)).thenReturn(response);

        ResponseEntity<AgendaResponseDto> responseEntity = agendaController.create(request);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnAgendas() {
        List<AgendaResponseDto> listDto = new ArrayList<>();
        listDto.add(new AgendaResponseDto("1","Agenda test 1"));
        listDto.add(new AgendaResponseDto("2","Agenda test 2"));

        Mockito.when(agendaService.findAll()).thenReturn(listDto);

        ResponseEntity<List<AgendaResponseDto>> responseEntity = agendaController.listAll();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(2, ((ArrayList<AgendaResponseDto>) responseEntity.getBody()).size());
    }
    
    @Test
    public void shouldReturnZeroAgendas() {
        ResponseEntity<List<AgendaResponseDto>> responseEntity = agendaController.listAll();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(0, ((LinkedList<AgendaResponseDto>) responseEntity.getBody()).size());
    }
}
