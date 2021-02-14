package com.avenue.associateassembly.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.repository.AgendaRepository;

@Service
public class AgendaServiceImpl implements AgendaService {

	private final AgendaRepository agendaRepository;
	private final ModelMapper modelMapper;

	@Autowired
	public AgendaServiceImpl(AgendaRepository agendaRepository, ModelMapper modelMapper) {
		this.agendaRepository = agendaRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public AgendaResponseDto createAgenda(AgendaRequestDto dto) {
		Agenda agenda = new Agenda(dto.getName());
		agenda = agendaRepository.insert(agenda);

		return modelMapper.map(agenda, AgendaResponseDto.class);
	}

}
