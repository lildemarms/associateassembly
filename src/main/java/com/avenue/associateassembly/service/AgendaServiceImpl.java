package com.avenue.associateassembly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;
import com.avenue.associateassembly.entity.Agenda;
import com.avenue.associateassembly.exception.NotFoundException;
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
	public AgendaResponseDto create(AgendaRequestDto dto) {
		Agenda agenda = new Agenda(dto.getName());
		agenda = agendaRepository.insert(agenda);

		return modelMapper.map(agenda, AgendaResponseDto.class);
	}

	@Override
	public AgendaResponseDto findById(String id) {
		Agenda agenda = this.agendaRepository.findById(new ObjectId(id)).
                orElseThrow(() -> new NotFoundException("Agenda not found."));

        return modelMapper.map(agenda, AgendaResponseDto.class);
	}

	@Override
	public List<AgendaResponseDto> findAll() {
		List<Agenda> agendaList = this.agendaRepository.findAll();

        return agendaList.stream().map(
            agenda -> modelMapper.map(agenda, AgendaResponseDto.class)
        ).collect(Collectors.toList());
	}

}
