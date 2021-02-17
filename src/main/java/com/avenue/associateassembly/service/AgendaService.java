package com.avenue.associateassembly.service;

import java.util.List;

import com.avenue.associateassembly.entity.Agenda;

public interface AgendaService {

	Agenda create(Agenda agenda);

	Agenda findById(String id);
	
	List<Agenda> findAll();
}
