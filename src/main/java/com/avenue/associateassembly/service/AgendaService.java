package com.avenue.associateassembly.service;

import com.avenue.associateassembly.dto.AgendaRequestDto;
import com.avenue.associateassembly.dto.AgendaResponseDto;

public interface AgendaService {
	
    AgendaResponseDto createAgenda(AgendaRequestDto dto);
}

