package com.avenue.associateassembly.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URISyntaxException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.service.AgendaService;

@RunWith(MockitoJUnitRunner.class)
public class AgendaControllerTest {

	@Mock
    public AgendaService agendaService;
	
	@Mock
	public ModelMapper modelMapper;

    @InjectMocks
    public AgendaController agendaController;
    
    @Before
	public void setup() {
		ReflectionTestUtils.setField(agendaController, "modelMapper", new ModelMapper());
	}
    
	@Test
    public void shouldCreateAgenda() throws URISyntaxException {
        String agendaName = "Agenda name";
        
        Agenda agendaServiceIn = new Agenda(agendaName);
        Agenda agendaServiceOut = new Agenda(new ObjectId(), agendaName);
        Mockito.when(agendaService.create(agendaServiceIn)).thenReturn(agendaServiceOut);

        AgendaRequestDto agendaRequest = new AgendaRequestDto(agendaName);
        ResponseEntity<AgendaResponseDto> responseEntity = agendaController.create(agendaRequest);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void shouldReturnAgendas() {
        List<Agenda> listDto = new ArrayList<>();
        listDto.add(new Agenda(new ObjectId(),"Agenda 1"));
        listDto.add(new Agenda(new ObjectId(),"Agenda 2"));

        Mockito.when(agendaService.findAll()).thenReturn(listDto);

        ResponseEntity<List<AgendaResponseDto>> responseEntity = agendaController.listAll();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(2, responseEntity.getBody().size());
    }
    
    @Test
    public void shouldReturnZeroAgendas() {
        ResponseEntity<List<AgendaResponseDto>> responseEntity = agendaController.listAll();

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertEquals(0, responseEntity.getBody().size());
    }
}
