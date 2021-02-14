package com.avenue.associateassembly.exception;

public class AgendaNotFoundException extends NotFoundException {

	private static final long serialVersionUID = 5485573518021618897L;

	public AgendaNotFoundException() {
		super("Agenda not found.");
	}
}
