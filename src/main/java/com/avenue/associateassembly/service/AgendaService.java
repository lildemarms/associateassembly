package com.avenue.associateassembly.service;

import java.util.List;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;

public interface AgendaService {

	AgendaResponseDto create(AgendaRequestDto dto);

	AgendaResponseDto findById(String id);
	
	List<AgendaResponseDto> findAll();
}
