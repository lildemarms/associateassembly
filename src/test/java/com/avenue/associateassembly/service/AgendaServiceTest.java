package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.springframework.test.util.ReflectionTestUtils;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.exception.AgendaNotFoundException;
import com.avenue.associateassembly.repository.AgendaRepository;

@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

	@Mock
	public AgendaRepository agendaRepository;

	@Mock
	public ModelMapper modelMapper;

	@InjectMocks
	public AgendaServiceImpl agendaService;

	@Before
	public void setup() {
		ReflectionTestUtils.setField(agendaService, "modelMapper", new ModelMapper());
	}

	@Test
	public void shouldCreateAgenda() {
		ObjectId id = new ObjectId();
		String name = "Agenda creation test";

		Agenda agenda = new Agenda(name);
		agenda.setId(id);

		Mockito.when(agendaRepository.insert(new Agenda(name))).thenReturn(agenda);

		AgendaRequestDto request = new AgendaRequestDto();
		request.setName(name);
		AgendaResponseDto response = agendaService.create(request);

		assertEquals(id.toHexString(), response.getId());
	}

	@Test
	public void shouldReturnZeroAgendas() {
		List<AgendaResponseDto> resp = agendaService.findAll();
		assertEquals(0, resp.size());
	}
	
	@Test
    public void shouldReturnOneAgenda() {
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("Agenda test 1");
        agenda.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));

        AgendaResponseDto response = agendaService.findById(id.toHexString());
        assertEquals(id.toHexString(), response.getId());
        assertEquals(agenda.getName(), response.getName());
    }
	
	@Test(expected = AgendaNotFoundException.class)
    public void shouldThrowNotFoundException(){
        ObjectId id = new ObjectId();
        Mockito.when(agendaRepository.findById(id)).thenThrow(new AgendaNotFoundException());

        agendaService.findById(id.toHexString());
    }
}
