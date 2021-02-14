package com.avenue.associateassembly.dto;

import com.avenue.associateassembly.entity.Answer;

public class VoteRequestDto {

	private String cpf;
	private Answer answer;

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
}
