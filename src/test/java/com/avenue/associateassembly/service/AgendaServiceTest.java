package com.avenue.associateassembly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.exception.AgendaNotFoundException;
import com.avenue.associateassembly.repository.AgendaRepository;

@RunWith(MockitoJUnitRunner.class)
public class AgendaServiceTest {

	@Mock
	public AgendaRepository agendaRepository;

	@InjectMocks
	public AgendaServiceImpl agendaService;

	@Test
	public void shouldCreateAgenda() {
		ObjectId id = new ObjectId();
		String name = "Agenda creation test";

		Agenda agendaInDB = new Agenda(id, name);
		Mockito.when(agendaRepository.insert(new Agenda(name))).thenReturn(agendaInDB);

		Agenda agenda = new Agenda(name);
		Agenda savedAgenda = agendaService.create(agenda);

		assertEquals(id.toHexString(), savedAgenda.getId().toHexString());
	}

	@Test
	public void shouldReturnZeroAgendas() {
		List<Agenda> resp = agendaService.findAll();
		assertEquals(0, resp.size());
	}
	
	@Test
    public void shouldReturnOneAgenda() {
        ObjectId id = new ObjectId();
        Agenda agenda = new Agenda("Agenda test 1");
        agenda.setId(id);

        Mockito.when(agendaRepository.findById(id)).thenReturn(java.util.Optional.of(agenda));

        Agenda response = agendaService.findById(id.toHexString());
        assertEquals(id.toHexString(), response.getId().toHexString());
        assertEquals(agenda.getName(), response.getName());
    }
	
	@Test(expected = AgendaNotFoundException.class)
    public void shouldThrowNotFoundException(){
        ObjectId id = new ObjectId();
        Mockito.when(agendaRepository.findById(id)).thenThrow(new AgendaNotFoundException());

        agendaService.findById(id.toHexString());
    }
}
