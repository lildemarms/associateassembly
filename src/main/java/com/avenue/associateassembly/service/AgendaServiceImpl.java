package com.avenue.associateassembly.service;

import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.exception.AgendaNotFoundException;
import com.avenue.associateassembly.repository.AgendaRepository;

@Service
public class AgendaServiceImpl implements AgendaService {

	Logger logger = LoggerFactory.getLogger(AgendaServiceImpl.class);

	private final AgendaRepository agendaRepository;

	@Autowired
	public AgendaServiceImpl(AgendaRepository agendaRepository) {
		this.agendaRepository = agendaRepository;
	}

	@Override
	public Agenda create(Agenda agenda) {
		return agendaRepository.insert(agenda);
	}

	@Override
	public Agenda findById(String id) {
		return this.agendaRepository.findById(new ObjectId(id)).
                orElseThrow(() -> agendaNotFound(id));
	}

	private AgendaNotFoundException agendaNotFound(String agendaId) {
		logger.info("Agenda (id: {}) not found.", agendaId);
		return new AgendaNotFoundException();
	}

	@Override
	public List<Agenda> findAll() {
		return this.agendaRepository.findAll();
	}

}
