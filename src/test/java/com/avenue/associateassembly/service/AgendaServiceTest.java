package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		AgendaResponseDto response = agendaService.createAgenda(request);

		assertEquals(id.toHexString(), response.getId());
	}
}
